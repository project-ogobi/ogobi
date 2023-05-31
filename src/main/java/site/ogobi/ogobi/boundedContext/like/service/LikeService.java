package site.ogobi.ogobi.boundedContext.like.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import site.ogobi.ogobi.boundedContext.comment.entity.Comment;
import site.ogobi.ogobi.boundedContext.like.controller.LikeController;
import site.ogobi.ogobi.boundedContext.like.entity.Like;
import site.ogobi.ogobi.boundedContext.like.repository.LikeRepository;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.post.entity.Post;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@RequestMapping("/posts")
public class LikeService {
    private final LikeRepository likeRepository;
    public void createLike(Member member, Post post) {
        post.getLike().add(member);

        Like like = Like.builder()
                .post(post)
                .member(member)
                .build();

        likeRepository.save(like);
    }

//    public boolean isLiked(Long likeId, Member member) {     //  이미 좋아요를 했으면 취소 시킴
//        Like like = likeRepository.findByMember(member);
//
//        if (like==null){
//            likeRepository.deleteById(likeId);
//            return false;
//        }
//        return true;
//    }
}
