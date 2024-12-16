package com.domain.demo_backend.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

// 사용자 정보와 관련된 =도메인 클래스, Builder 패턴사용해 객체 생성, username, password, role 필드 포함

@Setter
@Getter
public class User {
    private String userId;
    private String password;
    private String role;
    private String username;
    private String phone;
    private String email;
    private String Hashedpassword;

    private User(Builder builder){
        this.userId = builder.userId;
        this.password = builder.password;
        this.role = builder.role;
    }
    public static class Builder{
        private String userId;
        private String password;
        private String role;
        private String username;
        public Builder username(String username) {
            this.username = username;
            return this;
        }
        public Builder password(String password) {
            this.password = password;
            return this;
        }
        public Builder role(String role) {
            this.role = role;
            return this;
        }
        public User build(){
            return new User(this);
        }
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


}
