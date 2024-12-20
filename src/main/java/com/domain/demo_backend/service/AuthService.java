package com.domain.demo_backend.service;


import com.domain.demo_backend.mapper.UserMapper;
import com.domain.demo_backend.user.domain.User;
import com.domain.demo_backend.user.dto.LoginRequest;
import com.domain.demo_backend.user.dto.RegisterRequest;
import com.domain.demo_backend.util.JwtProvider;
import com.domain.demo_backend.util.PasswordUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {
    private final UserMapper userMapper;
    private final JwtProvider jwtProvider;

    public AuthService(UserMapper userMapper, JwtProvider jwtProvider) {
        this.userMapper = userMapper;
        this.jwtProvider = jwtProvider;
    }
    public String login(LoginRequest loginRequest) {

        User user = userMapper.findByUserId(loginRequest.getUserId());
        // 디버깅
        System.out.println("DB에서 가져온 User: " + user);
        System.out.println("LoginRequest UserID: " + loginRequest.getUserId());
        System.out.println("DB 해시값: " + user.getHashedPassword());
        System.out.println("입력된 해시값: " + PasswordUtil.sha256(loginRequest.getPassword()));

        if (user == null) {
            throw new RuntimeException("존재하지 않는 아이디입니다.");
        }
        if (!user.getHashedPassword().equals(PasswordUtil.sha256(loginRequest.getPassword()))) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        // 비밀번호를 포함하지 않은 사용자 정보를 JWT에 포함
        return jwtProvider.createToken(user.getUserId(), user.getRole());
    }


    // 새 사용자 정보를 해시처리 후 데이터베이스에 저장
    // 이미 존재하는 사용자 아이디인지 확인하고 중복되면 예외 발생
    @Transactional
    public void register(RegisterRequest registerRequest) {
        if (userMapper.findByUserId(registerRequest.getUserId()) != null) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        if(userMapper.findByUserEmail(registerRequest.getEmail()) != null){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        if(userMapper.findByUserPhone(registerRequest.getPhone()) != null){
            throw new IllegalArgumentException("이미 존재하는 핸드폰 번호입니다.");
        }

        User user = User.builder()
                .userId(registerRequest.getUserId())
                .username(registerRequest.getUsername())
                .password(registerRequest.getPassword())
                .hashedPassword(PasswordUtil.sha256(registerRequest.getPassword()))
                .phone(registerRequest.getPhone())
                .email(registerRequest.getEmail())
                .role("USER")
                .createdAt(LocalDateTime.now())
                .build();
        userMapper.insertUser(user);
    }
}
