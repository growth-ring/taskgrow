package com.growth.task.user.controller;

import com.growth.task.user.domain.Users;
import com.growth.task.user.dto.LoginRequest;
import com.growth.task.user.dto.LoginResponse;
import com.growth.task.user.service.AuthenticationService;
import com.growth.task.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/login")
@Tag(name = "User", description = "User API Document")
public class LoginController {
    private final UserService userService;
    private final AuthenticationService authService;

    public LoginController(UserService userService, AuthenticationService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping
    @ResponseStatus(OK)
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        Users user = userService.getByName(request.getName());
        Users loginUser = authService.login(user, request.getPassword());
        return new LoginResponse(loginUser.getName());
    }
}
