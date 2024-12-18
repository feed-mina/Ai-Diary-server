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
    private  UserMapper userMapper;
    private  JwtProvider jwtProvider;

    public AuthService(UserMapper userMapper, JwtProvider jwtProvider) {
        this.userMapper = userMapper;
        this.jwtProvider = jwtProvider;
    }
    public String login(LoginRequest loginRequest) {
        User user = userMapper.findByUserId(loginRequest.getUserId());
        if( user == null || !user.getPassword().equals(PasswordUtil.sha256(loginRequest.getPassword())) ) {
            throw new RuntimeException("이미 아이디나 비밀번호가 존재합니다");
        }
        return jwtProvider.createToken(user.getUsername(), user.getPassword());
    }


    // 새 사용자 정보를 해시처리 후 데이터베이스에 저장
    // 이미 존재하는 사용자 아이디인지 확인하고 중복되면 예외 발생
    public void register(RegisterRequest registerRequest) {
        if (userMapper.findByUserId(registerRequest.getUserId()) != null) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }
        if(userMapper.findByUserEmail(registerRequest.getEmail()) != null){
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        if(userMapper.findByUserPhone(registerRequest.getPhone()) != null){
            throw new RuntimeException("이미 존재하는 핸드폰 번호입니다.");
        }

        User user = User.builder()
                .userId(registerRequest.getUserId())
                .username(registerRequest.getUsername())
                .password(registerRequest.getPassword())
                .repassword(PasswordUtil.sha256(registerRequest.getPassword()))
                .phone(registerRequest.getPhone())
                .email(registerRequest.getEmail())
                .role("USER")
                .build();
        userMapper.insertUser(user);
    }
}
