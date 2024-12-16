package com.domain.demo_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class SecurityConfig {

    // 비밀번호 암호화 설정
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager 설정
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // Spring Security 필터 체인 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (테스트용)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/login", "/resources/**", "/static/**", "/error").permitAll() // 로그인 및 정적 리소스 접근 허용
                        .anyRequest().authenticated() // 그 외 요청은 인증 필요
                );
                /*
                *
                .formLogin(form -> form
                        .loginPage("/login") // 로그인 페이지 설정
                        .defaultSuccessUrl("/home", true) // 로그인 성공 시 이동할 페이지
                        .failureUrl("/login?error=true") // 로그인 실패 시 이동할 페이지
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout=true") // 로그아웃 성공 시 이동할 페이지
                        .permitAll()
                );  */

        return http.build();
    }
}
