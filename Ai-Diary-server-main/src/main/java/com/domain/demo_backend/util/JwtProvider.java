package com.domain.demo_backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtProvider {
    private String secretKey = "Keypair"; // 비밀키

    // JWT 생성
    public String createToken(String username, String role) {
        String token = Jwts.builder()
                .setSubject("testUser") // 테스트 유저
                .claim("role","admin") // 추가 데이터
                .setIssuedAt(new Date()) // 발급시간
                .setExpiration(new Date(System.currentTimeMillis()+3600000)) // 만료시간 (1시간 후)
                .signWith(SignatureAlgorithm.HS256, secretKey) // 서명
                .compact();

        System.out.println("Generated JWT : "+token);
        return token;
    }

    // JWT 검증 : 토큰 파싱, Claims 객체 반환, 잘못된 토큰 예외
    public Claims validateToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT", e);
        }
    }
}
