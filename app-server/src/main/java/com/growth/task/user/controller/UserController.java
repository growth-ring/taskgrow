package com.growth.task.user.controller;

import com.growth.task.user.dto.UserGetResponse;
import com.growth.task.user.dto.UserSignUpRequest;
import com.growth.task.user.dto.UserSignUpResponse;
import com.growth.task.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public UserSignUpResponse create(@RequestBody @Valid UserSignUpRequest request) {
        return userService.save(request);
    }

    @GetMapping("/{name}")
    @ResponseStatus(OK)
    public UserGetResponse getUser(@PathVariable("name") String name) {
        return userService.getByName(name);
    }
}
