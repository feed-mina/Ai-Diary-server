package com.domain.demo_backend.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class UserServiceConfig {

    private final PasswordEncoder passwordEncoder;

    public UserServiceConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    // InMemoryUserDetailsManager로 사용자 관리 설정

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("testUser").password(passwordEncoder.encode("testPassword"))
                .roles("USER")
                .build());
        return manager;
    }

    // AuthenticationManager 설정

    @Bean
    @Primary
    public AuthenticationManagerBuilder authenticationManagerBuilder() throws Exception {
        AuthenticationManagerBuilder  auth = new AuthenticationManagerBuilder(null);
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder);
        return auth;
     }
}
