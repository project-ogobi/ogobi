package site.ogobi.ogobi.base.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.member.repository.MemberRepository;

import java.util.Map;

@Service
public class PrincipalOauthUserService extends DefaultOAuth2UserService {
    @Autowired
    private MemberRepository memberRepository;

    // Resource Server로부터 받은 userRequest 데이터에 대한 후처리
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // registraionId로 어떤 OAuth로 로그인 했는지 확인
        System.out.println("getClientRegistration: " + userRequest.getClientRegistration());
        System.out.println("getAccessToken: " + userRequest.getAccessToken().getTokenValue());
        System.out.println("getAttributes: " + super.loadUser(userRequest).getAttributes());
        // 소셜 로그인 버튼 클릭 -> 소셜 로그인 창 -> 로그인 완료 -> code를 리턴(OAuth - Clien 라이브러리가 받아줌) -> code를 통해서 AcssToken요청 (access토큰 받음)
        // => "userRequest"가 가지고 있는 정보
        // 회원 프로필을 받을 때 사용되는 "loadUser" 함수 -> Resource Server로부터 정보를 받을 수 있다.

        // OAuth 로그인 회원 가입
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = null;

        if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            oAuth2UserInfo = new KakaoUserInfo((Map)oAuth2User.getAttributes().get("kakao_account"),
                    String.valueOf(oAuth2User.getAttributes().get("id")));
        } else {
            System.out.println("지원하지 않는 로그인 서비스 입니다.");
        }

        String providerType = oAuth2UserInfo.getProviderType();
        String providerId = oAuth2UserInfo.getProviderId();
        String username = providerType + "_" + providerId;
        String password = "test"; // TODO: 복호화
        String email = oAuth2UserInfo.getEmail();

        Member memberEntity = memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username(%s) not found".formatted(username))); // TODO: Optional을 예외처리하여 Member로 받게되면 stackoverflow
        // 처음 서비스를 이용한 회원일 경우
        if (memberEntity == null) {
            memberEntity = Member.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .providerType(providerType)
                    .provideId(providerId)
                    .nickname(username)
                    .build();
            memberRepository.save(memberEntity);
        }

        return new PrincipalDetails(memberEntity, oAuth2User.getAttributes());
    }
}