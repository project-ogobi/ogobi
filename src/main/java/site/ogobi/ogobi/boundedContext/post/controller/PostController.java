package site.ogobi.ogobi.boundedContext.post.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import site.ogobi.ogobi.base.rq.Rq;
import site.ogobi.ogobi.boundedContext.challenge.entity.Challenge;
import site.ogobi.ogobi.boundedContext.challenge.service.ChallengeService;
import site.ogobi.ogobi.boundedContext.comment.dto.CommentDto;
import site.ogobi.ogobi.boundedContext.like.entity.Like;
import site.ogobi.ogobi.boundedContext.like.service.LikeService;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.member.service.MemberService;
import site.ogobi.ogobi.boundedContext.post.dto.PostDto;
import site.ogobi.ogobi.boundedContext.post.entity.ChallengePost;
import site.ogobi.ogobi.boundedContext.post.entity.Post;
import site.ogobi.ogobi.boundedContext.post.repository.ChallengePostRepository;
import site.ogobi.ogobi.boundedContext.post.service.PostService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final Rq rq;
    private final PostService postService;
    private final MemberService memberService;
    private final LikeService likeService;
    private final ChallengeService challengeService;
    private final ChallengePostRepository challengePostRepository;


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{category}/detail/{id}")
    public String showPost(Model model, @PathVariable String category, @PathVariable Long id, HttpServletRequest request, HttpServletResponse response, PostDto postDto, CommentDto commentDto) {
        Post post = postService.getPost(id);
        Member member = rq.getMember();
        Like like = likeService.findByMemberIdAndPostId(member.getId(), id).orElse(null);

        model.addAttribute("commentDto", commentDto);

        boolean isLiked = false;
        if (like != null && like.getPost()==post){
            isLiked = true;
        }

        // 조회수 중복 방지
        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("postView")) {
                    oldCookie = cookie;
                }
            }
        }
        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("["+ id.toString() +"]")) {
                this.postService.incleaseView(id);
                oldCookie.setValue(oldCookie.getValue() + "_[" + id + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24); // 쿠키 유효기간 (1일)
                response.addCookie(oldCookie);
            }
        } else {
            this.postService.incleaseView(id);
            Cookie newCookie = new Cookie("postView", "[" + id + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            response.addCookie(newCookie);
        }

        model.addAttribute("post", post);
        model.addAttribute("isLiked", isLiked);

        if (category.equals("sharing")){
            ChallengePost challengePost = challengePostRepository.findByPostId(post.getId()).orElse(null);

            if (challengePost==null){
                return rq.historyBack("챌린지 post가 아닙니다.");
            }

            model.addAttribute("challenge", challengePost.getChallenge());
            return "post/share";
        }
        return "post/detail";
    }

    @GetMapping("/{category}/list")
    public String showList(Model model, @PathVariable String category, @RequestParam(value = "page", defaultValue = "0") int page) {
        Post.Category postCategory = getCategory(category);
        Page<Post> paging = this.postService.getListByCategory(postCategory, page);
        model.addAttribute("paging", paging);
        return "post/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{category}/create")
    public String showCreate(Model model, @PathVariable String category, PostDto postDto) {
        Member member = rq.getMember();

        model.addAttribute("category", category);

        if (category.equals("sharing")){
            List<Challenge> challengeList = challengeService.findByMember(member);
            if (challengeList.isEmpty()){
                return rq.historyBack("공유할 챌린지가 없습니다.");
            }

            List<Challenge> challenges = challengeService.findByMember(member);

            model.addAttribute("challengeList", challengeList);
            model.addAttribute("challenges", challenges);
            model.addAttribute("postDto", postDto);
            return "post/share_create";
        }
        return "post/create";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{category}/create")
    public String create(@Valid PostDto postDto, BindingResult bindingResult, @PathVariable String category, Principal principal) {

        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessage.append(error.getDefaultMessage()).append("<br>");
            }
            return rq.historyBack(errorMessage.toString());
        }
        Post.Category postCategory = getCategory(category);
        Member member = this.memberService.getMember(principal.getName());



        this.postService.create(postDto.getSubject(), postDto.getContent(), postCategory, member, postDto.getChallengeId());

        return String.format("redirect:/posts/%s/list", category);
    }

    private Post.Category getCategory(String category) {
        if ("free".equals(category)) {
            return Post.Category.FREE;
        } else if ("sharing".equals(category)) {
            return Post.Category.SHARING;
        }
        throw new IllegalArgumentException("Invalid category: " + category);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{category}/modify/{id}")
    public String showModify(@PathVariable String category, @PathVariable Long id, PostDto postDto) {
        Post post = this.postService.getPost(id);
        postDto.setSubject(post.getSubject());
        postDto.setContent(post.getContent());
        return "post/create";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{category}/modify/{id}")
    public String modify(@PathVariable String category, @PathVariable Long id, Principal principal, @Valid PostDto postDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessage.append(error.getDefaultMessage()).append("<br>");
            }
            return rq.historyBack(errorMessage.toString());
        }
        this.postService.modify(id, postDto.getSubject(), postDto.getContent(), principal.getName());
        return String.format("redirect:/posts/%s/detail/%s", category, id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{category}/delete/{id}")
    public String delete(@PathVariable String category, @PathVariable Long id, Principal principal) {
        this.postService.delete(id, principal.getName());
        return String.format("redirect:/posts/%s/list", category);
    }

    @GetMapping("/main")
    public String showMain(Model model){
        List<Post> bestPosts = postService.bestPostList();
        List<Post> resentPostList = postService.resentPostList();

        model.addAttribute("bestPosts", bestPosts);
        model.addAttribute("resentPostList", resentPostList);
        return "post/main";
    }

}