package com.growth.task.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.growth.task.user.domain.Users;
import com.growth.task.user.domain.UsersRepository;
import com.growth.task.user.domain.type.Role;
import com.growth.task.user.dto.LoginRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class LoginControllerTest {
    public static final String USER_NAME = "grow";
    public static final String PASSWORD = "password";
    @Autowired
    private UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @AfterEach
    void cleanUp() {
        usersRepository.deleteAll();
    }

    @Nested
    @DisplayName("POST /api/v1/login 요청은")
    class Describe_POST {
        private ResultActions subject(LoginRequest request) throws Exception {
            return mockMvc.perform(post("/api/v1/login")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
            );
        }

        @Nested
        @DisplayName("올바른 name과 password가 주어지면")
        class Context_with_name_and_password {
            private LoginRequest request;

            @BeforeEach
            void prepare() {
                createUser();
                request = LoginRequest.builder()
                        .name(USER_NAME)
                        .password(PASSWORD)
                        .build();
            }

            @Test
            @DisplayName("로그인되고, 200을 응답한다")
            void it_response_200() throws Exception {
                final ResultActions resultActions = subject(request);

                resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("name").value("grow"))
                        .andExpect(jsonPath("role").value("USER"))
                ;
            }
        }

        @Nested
        @DisplayName("올바르지 않은 password가 주어지면")
        class Context_with_invalid_password {
            private LoginRequest request;
            String invalidPassword = "xx" + PASSWORD;

            @BeforeEach
            void prepare() {
                createUser();
                request = LoginRequest.builder()
                        .name(USER_NAME)
                        .password(invalidPassword)
                        .build();
            }

            @Test
            @DisplayName("401을 응답한다")
            void it_response_401() throws Exception {
                final ResultActions resultActions = subject(request);

                resultActions.andExpect(status().isUnauthorized())
                ;
            }
        }
    }

    private void createUser() {
        usersRepository.save(Users.builder()
                .name(USER_NAME)
                .password(passwordEncoder.encode(PASSWORD))
                .role(Role.USER)
                .build()
        );
    }
}
