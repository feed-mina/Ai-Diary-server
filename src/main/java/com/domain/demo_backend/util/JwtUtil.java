package com.domain.demo_backend.util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Date;


@Component
public class JwtUtil {

    private SecretKey secretKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.secret-key}")
    private String secret;

//    private static final String SCRET_kEY = "${jwt.secret-key}";

    private static final String SECRET_KEY = "mySuperSecretKey"; // 🔹 비밀키 (32바이트)
    private static final String SALT = "mySaltValue"; // 🔹 SALT 값

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
    public String createToken(String username, BigInteger userSqno, String userId) {
        Claims claims = Jwts.claims().setSubject(username);
        Date now = new Date();
        Date validity = new Date(now.getTime() + 1000 * 60 * 60 * 24);

        return Jwts.builder()
                .setClaims(claims)
                .claim("userSqno", userSqno.toString()) //
                .claim("userId", userId)// 사용자 고유 식별자 추가
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

/** AES-256으로 문자열을 암호화 하는 메서드*/
public static String encryptAesByBase64(String strToEncrypt){
        try {
        byte[] iv = new byte[16]; // 초기화 백터 (IV)
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        SecretKeySpec secretKey = getSecretKey();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);

        byte[] encryptedText = cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8));

        //Base64 인코딩 후 반환
            return Base64.getEncoder().encodeToString(encryptedText);

    } catch (Exception e) {
            e.printStackTrace();
        }
    return null;
}
// AES56으로 암호화된 문자열을 복호화 하는 메서드
    public static String decryptAesByBase64(String strToDecrypt){
    try{
        byte[] iv = new byte[16]; // 초기화 백터 (IV)
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        SecretKeySpec secretKey = getSecretKey();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);

        // Base64 디코딩 후 복호화
        byte[] decodedText = Base64.getDecoder().decode(strToDecrypt);
        return new String(cipher.doFinal(decodedText), StandardCharsets.UTF_8);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}


//      * AES-256 비밀키 생성 메서드
private static SecretKeySpec getSecretKey() throws Exception{
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
    SecretKey secretKey = factory.generateSecret(spec);
    return new SecretKeySpec(secretKey.getEncoded(), "AES");
}

}
