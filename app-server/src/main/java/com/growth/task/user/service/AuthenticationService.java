package com.growth.task.user.service;

import com.growth.task.user.domain.Users;
import com.growth.task.user.exception.AuthenticationFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Users login(Users user, String password) {
        if (!user.authenticate(password, passwordEncoder)) {
            throw new AuthenticationFailureException();
        }
        return user;
    }
}
