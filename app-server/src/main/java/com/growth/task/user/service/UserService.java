package com.growth.task.user.service;

import com.growth.task.user.domain.Users;
import com.growth.task.user.domain.UsersRepository;
import com.growth.task.user.dto.UserSignUpRequest;
import com.growth.task.user.dto.UserSignUpResponse;
import com.growth.task.user.exception.UserNameDuplicationException;
import com.growth.task.user.exception.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserSignUpResponse signUp(UserSignUpRequest request) {
        String name = request.getName();
        if (isExistedName(name)) {
            throw new UserNameDuplicationException(name);
        }
        Users save = saveUser(request.toEntity());
        return UserSignUpResponse.of(save);
    }

    @Transactional(readOnly = true)
    public Users getByName(String name) {
        return usersRepository.findByName(name)
                .orElseThrow(() -> new UserNotFoundException(name));
    }

    @Transactional(readOnly = true)
    private boolean isExistedName(String name) {
        return usersRepository.existsByName(name);
    }

    @Transactional
    private Users saveUser(Users user) {
        user.modifyPassword(passwordEncoder);
        return usersRepository.save(user);
    }
}
