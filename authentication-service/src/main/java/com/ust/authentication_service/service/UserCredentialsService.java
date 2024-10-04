package com.ust.authentication_service.service;

import com.ust.authentication_service.dao.UserCredentialsDao;
import com.ust.authentication_service.dao.entity.UserCredentialsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserCredentialsService {
    @Autowired
    JwtService jwtService;
    @Autowired
    UserCredentialsDao authDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public UserCredentialsEntity register(UserCredentialsEntity user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return authDao.saveAndFlush(user);
    }
    public String generateToken(String name, String role) {
        return jwtService.generateToken(name, role);
    }
    public boolean verifyToken(String token) {
        jwtService.validateToken(token);
        return true;
    }
}
