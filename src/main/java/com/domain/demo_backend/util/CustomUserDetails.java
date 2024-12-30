package com.domain.demo_backend.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigInteger;
import java.util.Collection;


@Getter
@Setter
public class CustomUserDetails extends User {
    // Spring Security의 SecurityContextHolder에서 인증 정보 가져오기
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    private String username;
    private BigInteger userSqno;
    private String userId;

    // Constructor for CustomUserDetails
    public CustomUserDetails(String username, BigInteger userSqno, String userId, Collection<? extends GrantedAuthority> authorities) {
        // Call the superclass (User) constructor
        super(username, userId, authorities);
        this.username = username;
        this.userId = userId;
        this.userSqno = userSqno;
    }


    public static CustomUserDetails getMemberInfo() {
        // Spring Security의 SecurityContextHolder에서 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
System.out.println("authentication.getPrincipal(): "+ authentication.getPrincipal());
        // 인증 여부 확인
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalStateException("인증되지 않은 사용자입니다.");
        }
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails)) {
            throw new IllegalStateException("사용자 정보가 유효하지 않습니다.");
        }
        // 사용자 정보를 CustomUserDetails로 반환
        return (CustomUserDetails) principal;
    }
}
