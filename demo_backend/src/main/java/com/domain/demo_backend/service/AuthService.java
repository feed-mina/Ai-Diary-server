package com.domain.demo_backend.service;


import com.domain.demo_backend.mapper.UserMapper;
import com.domain.demo_backend.user.domain.User;
import com.domain.demo_backend.user.dto.LoginRequest;
import com.domain.demo_backend.user.dto.RegisterRequest;
import com.domain.demo_backend.util.JwtProvider;
import com.domain.demo_backend.util.PasswordUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserMapper userMapper;
    private final JwtProvider jwtProvider;

    public AuthService(UserMapper userMapper, JwtProvider jwtProvider) {
        this.userMapper = userMapper;
        this.jwtProvider = jwtProvider;
    }
    public String login(LoginRequest loginRequest) {
        User user = userMapper.findByUsername(loginRequest.getUsername());
        if( user == null || !user.getPassword().equals(PasswordUtil.sha256(loginRequest.getPassword())) ) {
            throw new RuntimeException("username or password is incorrect");
        }
        return jwtProvider.createToken(user.getUsername(), user.getPassword());
    }


    // 새 사용자 정보를 해시처리 후 데이터베이스에 저장
    // 이미 존재하는 사용자 이름인지 확인하고 중복되면 예외 발생
    public void register(RegisterRequest registerRequest) {
        if (userMapper.findByUsername(registerRequest.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        User user = new User.Builder()
                .username(registerRequest.getUsername())
                .password(PasswordUtil.sha256(registerRequest.getPassword()))
                .role("USER")
                .build();
        userMapper.insertUser(user);
    }
}
