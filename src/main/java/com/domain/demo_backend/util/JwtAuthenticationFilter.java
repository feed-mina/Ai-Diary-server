package com.domain.demo_backend.util;

import com.domain.demo_backend.config.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

//    @Autowired
//  private JwtUtil JwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        System.out.println("Authorization Header: " + authorizationHeader);

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            try {
                Claims claims = jwtUtil.validateToken(token); // 토큰 검증
                // 유효하지 않은 토큰 예외 처리
                System.out.println("claims: " + claims);
                // String username = claims.get("username", String.class);
                String username = claims.getSubject();
                String userSqnoStr = claims.get("userSqno", String.class); // String으로 읽기
                BigInteger userSqno = new BigInteger(userSqnoStr); // BigInteger로 변환

                System.out.println("username: " + username);

                System.out.println("userSqno: " + userSqno);

                if (username != null) {
//                    UserDetails userDetails = User.withUsername(username)
//                            .password("") // 비밀번호는 확인하지 않음
//                            .authorities("ROLE_USER") // 역할 설정
//                            .build();
                    // 사용자 권한 설정

                    List<GrantedAuthority> authorities = List.of(() -> "ROLE_USER");
                    // CustomUserDetails 객체 생성

                    CustomUserDetails userDetails = new CustomUserDetails(
                                username, userSqno, "", List.of(() -> "ROLE_USER"));
                    // 인증 토큰 생성

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // SecurityContextHolder에 인증 정보 저장

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception e) {
                // 유효하지 않은 토큰 예외 처리
                System.err.println("Invalid JWT token: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 응답 반환
                response.getWriter().write("Invalid JWT Token");
                return;
            }
        }
        filterChain.doFilter(request, response); // 다음 필터로 이동
    }


}
