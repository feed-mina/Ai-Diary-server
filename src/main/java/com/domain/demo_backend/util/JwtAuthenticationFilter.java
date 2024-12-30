package com.domain.demo_backend.util;

import com.domain.demo_backend.config.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

            System.out.println("token : " + token);
            try {
                Claims claims = jwtUtil.validateToken(token); // 토큰 검증
                // 유효하지 않은 토큰 예외 처리
                System.out.println("claims: " + claims);
                String username = claims.getSubject();
                System.out.println("username : " + username);
                String userId = claims.get("userId", String.class);
                String userSqnoStr = claims.get("userSqno", String.class); // String으로 읽기
                System.out.println("userSqnoStr : " + userSqnoStr);
                BigInteger userSqno = new BigInteger(userSqnoStr);
                System.out.println("userSqno : " + userSqno);

                if (username != null) {
                    List<GrantedAuthority> authorities = List.of(() -> "ROLE_USER");
                    System.out.println("authorities: " + authorities);
                    CustomUserDetails userDetails = new CustomUserDetails(username, userSqno, userId, List.of(new SimpleGrantedAuthority("ROLE_USER")));

                    System.out.println("userDetails: " + userDetails);
                    // 인증 토큰 생성
                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    System.out.println("authentication: " + authentication);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // 유효하지 않은 토큰 예외 처리
                System.err.println("Invalid JWT token: " + e.getMessage());
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 응답 반환
                response.getWriter().write("Invalid JWT Token");
                return;
            }
        }
        filterChain.doFilter(request, response); // 다음 필터로 이동
        System.out.println("request: " + request);
        System.out.println("response: " + response);
    }


}
