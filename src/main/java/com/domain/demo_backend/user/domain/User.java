package com.domain.demo_backend.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDateTime;

// 사용자 정보와 관련된 =도메인 클래스, Builder 패턴사용해 객체 생성, username, password, role 필드 포함

@Setter
@Getter
@NoArgsConstructor
public class User {
    private BigInteger userSqno;         // user_sqno
    private String userId;              // user_id
    private String password;            // password
    private String hashedPassword;      // hashed_password
    private String role;                // role
    private String username;            // username
    private String phone;               // phone
    private String email;               // email
    private String repassword;          // repassword
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @Builder
    public User(String userId, String password, String hashedPassword, String role, String username, String phone, String email, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.password = password;
        this.hashedPassword = hashedPassword;
        this.role = role;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
