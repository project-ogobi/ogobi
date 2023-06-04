package site.ogobi.ogobi.boundedContext.post.service;

import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.post.entity.Post;
import site.ogobi.ogobi.boundedContext.post.repository.PostRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    public boolean isMyPost(Member member, Post post) {   //글 작성자가 본인인지 여부 판단
        String writer = post.getAuthor().getNickname();

        if (member.getNickname().equals(writer)) {
            return true;
        }
        return false;
    }

    public Post getPost(Long id) {
        Optional<Post> post = this.postRepository.findById(id);

        if (! post.isPresent()) {
            return null;
        }
        return post.get();
    }

    public Page<Post> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.postRepository.findAll(pageable);
    }
    public Page<Post> getListByCategory(Post.Category category, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return postRepository.findByCategory(category, pageable);
    }

    @Transactional
    public void create(String subject, String content, Post.Category category, Member member) {
        Post p = Post.builder()
                .subject(subject)
                .content(content)
                .category(category)
                .author(member)
                .build();
        postRepository.save(p);
    }

    @Transactional
    public void modify(Long postId, String subject, String content, String username) {
        Post post = getPost(postId);
        if(!post.getAuthor().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        post.modify(subject, content);
    }

    @Transactional
    public void delete(Long postId, String username) {
        Post post = getPost(postId);
        if (!post.getAuthor().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        postRepository.delete(post);
    }


    public List<Post> bestPostList() {
        List<Post> sortLike = postRepository.findAll(Sort.by(Sort.Direction.DESC, "like"));
        LocalDate oneWeekAgo = LocalDate.now().minus(7, ChronoUnit.DAYS);   //  오늘부터 7일 전
        List<Post> periodPosts = new ArrayList<>(); //  7일 전에 올라온 게시글 중 추천 수가 가장 높은 게시글을 담을 리스트

        for (Post post:sortLike) {
            if (post.getCreateDate().isAfter(oneWeekAgo.atStartOfDay())){
                periodPosts.add(post);    //  오늘부터 7일 이내에 작성된 게시글이라면 리스트에 추가
            }
        }
        
        int count = Math.min(periodPosts.size(), 5); //  5개나, 5개 미만이면 다 넘긴다.

        return periodPosts.subList(0, count);
    }
}
