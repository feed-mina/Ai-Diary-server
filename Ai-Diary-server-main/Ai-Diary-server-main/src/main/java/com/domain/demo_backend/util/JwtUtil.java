package com.domain.demo_backend.util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;


@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.issuer}")
    private String issuer;

    private static final String SCRET_kEY = "secret";

    public String generateToken(String userId, boolean useKakaoIssuer) {
        String tokenIssuer = useKakaoIssuer ? "https://kauth.kakao.com" : this.issuer; // Kakao 발급자 사용 여부에 따라 설정
        return Jwts.builder()
                .setSubject(userId)
                .setIssuer(issuer)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
