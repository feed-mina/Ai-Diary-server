package com.domain.demo_backend.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String userId;
    private String username;
    private String password;
    private String repassword;
    private String role;
    private String phone;
    private String email;
}
