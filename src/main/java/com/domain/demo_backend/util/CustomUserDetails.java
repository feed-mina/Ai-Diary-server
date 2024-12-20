package com.domain.demo_backend.util;

import com.domain.demo_backend.user.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final User memberInfo;
    public CustomUserDetails(User memberInfo){
        this.memberInfo = memberInfo;
    }

    public User getMemberInfo() {
        return memberInfo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return null;
    }

    @Override
    public String getPassword() {
        return memberInfo.getPassword();
    }

    @Override
    public String getUsername() {
        return memberInfo.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
