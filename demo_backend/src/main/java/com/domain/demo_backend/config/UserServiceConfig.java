package com.domain.demo_backend.config;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceConfig {

    private final PasswordEncoder passwordEncoder;

    public UserServiceConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("testUser")
                .password(passwordEncoder.encode("testPassword"))
                .roles("USER");
    }
}
