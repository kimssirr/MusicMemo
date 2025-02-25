package music.memo.config;

import music.memo.Service.CustomUserDetailService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.HiddenHttpMethodFilter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    // 1) 스프링 시큐리티의 모든 기능 비활성화 (H2 콘솔 관련 요청 비활성화)
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers("/css/**");
    }

    // 2) 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/login", "/public/**").permitAll() // 인증 없이 접근할 경로 설정
                        .requestMatchers("/user/**","/music/**","/review/**").hasAuthority("USER") // 유저 권한 경로 허용
                        .anyRequest().authenticated() // 나머지는 인증 필요
                )
                .formLogin((auth) -> auth
                        .loginPage("/public/loginForm")   // 사용자 정의 로그인 페이지
                        .defaultSuccessUrl("/", true) // 로그인 성공 후 이동 페이지
                        .failureUrl("/public/loginForm?error=true")  // 로그인 실패 시 경로 설정
                        .loginProcessingUrl("/login") // 로그인 form action url
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")          // 로그아웃 URL
                        .logoutSuccessUrl("/")         // 로그아웃 후 기본 페이지로 리디렉션
                        .invalidateHttpSession(true)   // 세션 무효화
                )
                .csrf((auth) -> auth.disable())                    // CSRF 비활성화
                .build();
    }


    // 7) 인증 관리자 관련 설정
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    // 9) 패스워드 인코더로 사용할 Bean 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    } //  bcrypt 강력 해싱 함수로 암호를 인코딩한다

    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }
}
