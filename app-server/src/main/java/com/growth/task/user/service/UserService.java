package com.growth.task.user.service;

import com.growth.task.task.exception.UserNotFoundException;
import com.growth.task.user.domain.Users;
import com.growth.task.user.domain.UsersRepository;
import com.growth.task.user.dto.UserGetResponse;
import com.growth.task.user.dto.UserSignUpRequest;
import com.growth.task.user.dto.UserSignUpResponse;
import com.growth.task.user.exception.UserNameDuplicationException;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UsersRepository usersRepository;

    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public UserSignUpResponse save(UserSignUpRequest request) {
        String name = request.getName();
        if (isExistedName(name)) {
            throw new UserNameDuplicationException(name);
        }
        Users user = usersRepository.save(request.toEntity());
        return UserSignUpResponse.of(user);
    }

    public UserGetResponse getByName(String name) {
        Users user = usersRepository.findByName(name)
                .orElseThrow(() -> new UserNotFoundException(name));
        return UserGetResponse.of(user);
    }

    private boolean isExistedName(String name) {
        return usersRepository.existsByName(name);
    }
}
