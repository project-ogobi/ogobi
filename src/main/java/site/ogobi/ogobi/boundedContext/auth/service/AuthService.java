package site.ogobi.ogobi.boundedContext.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ogobi.ogobi.boundedContext.auth.entity.SignUp;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.member.repository.MemberRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(SignUp signUp) {
        // 중복 이메일 체크
        Optional<Member> userEmail = memberRepository.findByEmail(signUp.getEmail());
        if (userEmail.isPresent()) {
//            throw new IllegalArgumentException();
            return;
        }
        String encodedPassword = passwordEncoder.encode(signUp.getPassword());

        Member member = Member.builder()
                .nickname(signUp.getNickname())
                .username(signUp.getLoginId())
                .password(encodedPassword)
                .email(signUp.getEmail())
                .providerType("Local") // 일반 회원가입 회원
                .build();
        memberRepository.save(member);
    }
}
