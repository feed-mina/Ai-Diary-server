package com.domain.demo_backend.user.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchConnectionDetails;
import org.springframework.data.annotation.Id;

import java.math.BigInteger;

// 사용자 정보와 관련된 =도메인 클래스, Builder 패턴사용해 객체 생성, username, password, role 필드 포함

@Setter
@Getter
@NoArgsConstructor
public class User {
    private BigInteger userSqno;
    private String userId;
    private String password;
    private String repassword;
    private String role;
    private String username;
    private String phone;
    private String email;
    private String Hashedpassword;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Builder
    public User(String userId, String password, String repassword, String role, String username, String phone, String email) {
        this.userId = userId;
        this.password = password;
        this.repassword = repassword;
        this.role = role;
        this.username = username;
        this.phone = phone;
        this.email = email;
    }

}
