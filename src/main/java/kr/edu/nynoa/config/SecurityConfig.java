package kr.edu.nynoa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // Spring Security 5.7.0-M2 부터 기존 WebSecurityConfigureAdapter 방식에서 SecurityFilterChain 으로 변경 권장
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 길고 길었던 CORS 에러.... 해결은 http.cors(); http.csrf().disable();
//        http.cors();
        http.csrf().disable();
        return http.build();
//        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}