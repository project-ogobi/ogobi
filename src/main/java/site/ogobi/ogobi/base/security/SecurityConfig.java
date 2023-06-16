package site.ogobi.ogobi.base.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import site.ogobi.ogobi.boundedContext.oauth2.PrincipalOauthUserService;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final PrincipalOauthUserService principalOauthUserService;
    private final PrincipalService principalService;

    // 로그인 인증에 상관없이 가져와야 할 페이지들
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/favicon.ico")
                .requestMatchers("/error");
    }

    // 권한에 따라 허용하는 url 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // /login, /signup 페이지는 모두 허용, 다른 페이지는 인증된 사용자만 허용
        http
                .authorizeHttpRequests()
                .requestMatchers(
                        new AntPathRequestMatcher("/**")
                ).permitAll()
                .anyRequest().authenticated();

        // login 설정
        http
                .formLogin()
                .loginPage("/auth/login")    // GET: 로그인 폼을 보여줌
                .usernameParameter("username")    // 로그인에 필요한 아이디
                .passwordParameter("password")    // 로그인에 필요한 password 값
                .defaultSuccessUrl("/");    // 로그인에 성공하면 /로 redirect
        // 소셜로그인 설정
        http
                .oauth2Login()
                .loginPage("/auth/login")
                .defaultSuccessUrl("/")
                .userInfoEndpoint()
                .userService(principalOauthUserService); // 소셜 로그인이 완료된 뒤의 소셜 회원의 엑세스 토큰, 사용자 정보를 받아옴
        // logout 설정
        http
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"))
                .logoutSuccessUrl("/")    // 로그아웃에 성공하면 /로 redirect
                .invalidateHttpSession(true); // 세션 삭제

        http
                .userDetailsService(principalService);

        return http.build();

    }

    //  비밀번호 찾기 설정 등록
    @Bean
    public PasswordResetConfig passwordResetConfig() {
        return new PasswordResetConfig();
    }

    // 로그인 방식 등록
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration ac) throws Exception {
        return ac.getAuthenticationManager();
    }
}