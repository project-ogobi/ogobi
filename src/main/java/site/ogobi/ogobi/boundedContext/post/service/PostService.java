package site.ogobi.ogobi.boundedContext.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.post.entity.Post;
import site.ogobi.ogobi.boundedContext.post.repository.PostRepository;

import java.time.LocalDateTime;
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

        if (!post.isPresent()) {
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
                .createDate(LocalDateTime.now())
                .build();
        postRepository.save(p);
    }

    @Transactional
    public void modify(Post post, String subject, String content) {
        post.modify(subject, content);
        this.postRepository.save(post);
    }
}
