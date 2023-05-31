package site.ogobi.ogobi.boundedContext.member.service;

import com.amazonaws.services.kms.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.member.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    @Transactional
    public Member join(String username, String password, String nickname) {
        if (memberRepository.findByUsername(username).isPresent()) {
            return null;
        }
        Member member = Member
                .builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .build();
        memberRepository.save(member);

        return member;
        }

    public Member getMember(String username) {
        Optional<Member> member = this.memberRepository.findByUsername(username);
        if (member.isPresent()) {
            return member.get();
        } else {
            throw new NotFoundException("member not found");
        }

    }
}