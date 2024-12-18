package com.domain.demo_backend.mapper;

import com.domain.demo_backend.user.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User findByUserId(String userId);
    User findByUserPhone(String phone);
    User findByUserEmail(String email);
    void insertUser(User user);

}
