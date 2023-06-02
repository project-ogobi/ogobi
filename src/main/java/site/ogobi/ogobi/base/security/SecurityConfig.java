package site.ogobi.ogobi.base.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    // 로그인 인증에 상관없이 가져와야 할 페이지들
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/favicon.ico")
                .requestMatchers("/error");
    }

    // 권한에 따라 허용하는 url 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
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
                .loginPage("/login")    // GET: 로그인 폼을 보여줌
                .usernameParameter("username")    // 로그인에 필요한 아이디
                .passwordParameter("password")    // 로그인에 필요한 password 값
                .defaultSuccessUrl("/");    // 로그인에 성공하면 /로 redirect
        // logout 설정
        http
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/");    // 로그아웃에 성공하면 /로 redirect

        // 이외 설정
        http
                .userDetailsService(userDetailService());

        return http.build();

    }

    // 로그인 방식 등록
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration ac) throws Exception {
        return ac.getAuthenticationManager();
    }

    // 임시 테스트 데이터
    @Bean
    public UserDetailsService userDetailService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        String rawPassword = "test1";
        String encryptedPassword = new BCryptPasswordEncoder().encode(rawPassword);

        UserDetails user = User.withUsername("test1")
                .password(encryptedPassword)
                .roles("ADMIN")
                .build();
        manager.createUser(user);

        String rawPassword2 = "1234";
        String encryptedPassword2 = new BCryptPasswordEncoder().encode(rawPassword2);
        UserDetails user2 = User.withUsername("user1")
                .password(encryptedPassword2)
                .roles("ADMIN")
                .build();
        manager.createUser(user2);

        return manager;
    }

    // Password 암호화 방식 설정
    @Bean
    public PasswordEncoder passwordEncoderConfig() {
        return new BCryptPasswordEncoder();
    }

}