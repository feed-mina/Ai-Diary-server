package com.domain.demo_backend.mapper;

import com.domain.demo_backend.user.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User findByUsername(String userId);

    void insertUser(User user);
}
