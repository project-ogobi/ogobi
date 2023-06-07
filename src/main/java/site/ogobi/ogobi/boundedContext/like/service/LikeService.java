package site.ogobi.ogobi.boundedContext.like.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.ogobi.ogobi.boundedContext.like.entity.Like;
import site.ogobi.ogobi.boundedContext.like.repository.LikeRepository;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.post.entity.Post;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    public void createLike(Member member, Post post) {
        Like liked = likeRepository.findByMemberIdAndPostId(member.getId(), post.getId()).orElse(null);

        if (liked!=null && liked.getPost()==post) {
            likeRepository.deleteById(liked.getId());   //  이미 추천을 했다면 삭제한다.
        } else {
            Like like = Like.builder()
                    .post(post)
                    .member(member)
                    .build();
            likeRepository.save(like);
        }

    }

    public Optional<Like> findByMemberIdAndPostId(Long memberId, Long postId){
        return likeRepository.findByMemberIdAndPostId(memberId, postId);
    }

}
