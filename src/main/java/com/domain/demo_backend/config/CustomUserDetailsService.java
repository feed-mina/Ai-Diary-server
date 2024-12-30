package com.domain.demo_backend.config;

import com.domain.demo_backend.mapper.UserMapper;
import com.domain.demo_backend.user.domain.User;
import com.domain.demo_backend.util.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Autowired
    public CustomUserDetailsService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // db에서 조회
      //  CustomUserDetails user =  userMapper.findByUsername(username);

      User user =  userMapper.findByUsername(username);
        System.out.println("DB에서 조회된 사용자: " + user);
        System.out.println("CustomUserDetails userSqno: " + user.getUserSqno());
        System.out.println("CustomUserDetails username: " + user.getUserSqno());
        System.out.println("CustomUserDetails userId: " + user.getUserId());

        if (user == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }

        return new CustomUserDetails(
                user.getUsername(),
                user.getUserSqno(),
                user.getUserId(),
                getAuthorities(user.getRole())
        ); // CustomUserDetails 객체 반환
    }
    // 사용자 역할 권한 부여
    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return List.of(new SimpleGrantedAuthority(role));
    }



}
