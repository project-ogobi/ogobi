package site.ogobi.ogobi.boundedContext.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.post.entity.Post;
import site.ogobi.ogobi.boundedContext.post.repository.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private PostRepository postRepository;

    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    public boolean isMyPost(Member member, Post post){   //글 작성자가 본인인지 여부 판단
        String writer = post.getAuthor().getNickname();

        if (member.getNickname().equals(writer)){
            return true;
        }
        return false;
    }


    public Post getPost(Long id) {
        Optional<Post> post = this.postRepository.findById(id);
        if (post.isPresent()){
            return post.get();
        }
        return null;    //Todo 예외처리하기
    }

    public List<Post> getList() {
        return this.postRepository.findAll();
    }

}
