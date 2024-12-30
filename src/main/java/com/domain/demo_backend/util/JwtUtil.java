package com.domain.demo_backend.util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import java.math.BigInteger;
import java.util.Base64;
import java.util.Date;


@Component
public class JwtUtil {

    private SecretKey secretKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.secret-key}")
    private String secret;

    private static final String SCRET_kEY = "${jwt.secret-key}";

    @PostConstruct
    public void init(){
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }
//    public String generateToken(String userId, boolean useKakaoIssuer) {
//        String tokenIssuer = useKakaoIssuer ? "https://kauth.kakao.com" : this.issuer; // Kakao 발급자 사용 여부에 따라 설정
//        return Jwts.builder()
//                .setSubject(userId)
//                .setIssuer(issuer)
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
//                .signWith(SignatureAlgorithm.HS256, secretKey)
//                .compact();
//    }

    public String generateToken(String username, BigInteger userSqno) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userSqno", userSqno.toString()) // 사용자 고유 식별자 추가
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10시간 유효
                .signWith(secretKey, SignatureAlgorithm.HS256) // secretKey 사용
                .compact();
    }

    // 토큰생성
    public String createToken(String username, BigInteger userSqno) {
        Claims claims = Jwts.claims().setSubject(username);
        Date now = new Date();
        Date validity = new Date(now.getTime() + 1000 * 60 * 60 * 24);

        return Jwts.builder()
                .setClaims(claims)
                .claim("userSqno", userSqno.toString()) // 사용자 고유 식별자 추가
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public BigInteger getUserSqnoFromToken(String token) {
        Claims claims = validateToken(token); // 토큰 검증 및 클레임 추출
        // userSqno 값을 BigInteger로 변환
        String userSqnoStr = claims.get("userSqno", String.class); // String으로 클레임 읽기
        return new BigInteger(userSqnoStr); // BigInteger로 변환

    }

}
