package com.domain.demo.auth.service;


import com.domain.demo.auth.Entity.Login;
import com.domain.demo.auth.dto.LoginRequest;
import com.domain.demo.auth.mapper.LoginMapper;
import com.domain.demo.util.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

@Autowired
private LoginMapper loginMapper;
/*
public boolean authenticate(String username, String password) {
    LoginRequest user = LoginMapper.findByUsername();
    String hashedPassword = HashUtil.sha256(password);
    return user != null && user.getPassword().equals(hashedPassword);
}

*/
public void registerUser(String username, String password) {
    String hashedPassword = HashUtil.sha256(password);
    LoginRequest user = new LoginRequest();
    //login.setUsername(username);
    //login.setPassword(hashedPassword);
    //LoginMapper.insertUser(user);
}


}
