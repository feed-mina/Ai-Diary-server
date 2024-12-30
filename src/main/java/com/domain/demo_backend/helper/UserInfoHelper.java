package com.domain.demo_backend.helper;

import com.domain.demo_backend.user.domain.User;
import com.domain.demo_backend.util.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigInteger;

public class UserInfoHelper {
    // 현재 로그인된 사용자의 정보를 가져온다. @return MemberInfo 현재 사용자의 정보를 가져오는 객체

    public static CustomUserDetails getMemberInfo(){
        // spring security의 securityContextHolder에서 인증 정보를 가져온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalStateException("인증되지 않은 사용자입니다.");
        }

        // 인증 객체가 CustomUserDetails 타입인지 확인
        if (!(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new IllegalStateException("사용자 정보가 유효하지 않습니다.");
        }

        // 사용자 저보를 CustomUserDetails로부터 가져온다.
        // CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return (CustomUserDetails) authentication.getPrincipal();

    }
// 현재 사용자의 userSqno 일련번호를 가져온다. @Return BigIntiger 사용자 일련번호
    public static BigInteger getCurrentUserSeq(){
        return getMemberInfo().getUserSqno();
    }
}
