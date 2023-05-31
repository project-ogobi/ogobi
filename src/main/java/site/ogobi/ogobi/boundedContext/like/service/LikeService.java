package site.ogobi.ogobi.boundedContext.like.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.ogobi.ogobi.boundedContext.like.entity.Like;
import site.ogobi.ogobi.boundedContext.like.repository.LikeRepository;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.post.entity.Post;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    public void createLike(Member member, Post post) {
        post.getLike().add(member);
        member.getLike().add(post);

        Like like = Like.builder()
                .post(post)
                .member(member)
                .build();
        likeRepository.save(like);
    }

}
