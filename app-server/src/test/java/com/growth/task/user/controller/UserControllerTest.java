package com.growth.task.user.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.growth.task.user.domain.Users;
import com.growth.task.user.domain.UsersRepository;
import com.growth.task.user.dto.UserSignUpRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private UsersRepository usersRepository;
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
        objectMapper.registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @AfterEach
    void cleanUp() {
        usersRepository.deleteAll();
    }

    @Nested
    @DisplayName("POST /api/v1/users 요청은")
    class Describe_POST {
        private ResultActions subject(UserSignUpRequest request) throws Exception {
            return mockMvc.perform(post("/api/v1/users")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
            );
        }

        @Nested
        @DisplayName("name과 password가 주어지면")
        class Context_with_name_and_password {
            private final UserSignUpRequest request = UserSignUpRequest.builder()
                    .name("grow")
                    .password("password")
                    .build();

            @Test
            @DisplayName("User를 생성하고, 201을 응답한다")
            void it_response_201() throws Exception {
                final ResultActions resultActions = subject(request);

                resultActions.andExpect(status().isCreated())
                        .andExpect(jsonPath("name").value("grow"))
                ;
            }
        }

        @Nested
        @DisplayName("이미 존재하는 name과 password가 주어지면")
        class Context_with_exist_name_and_password {
            @BeforeEach
            void setUp() {
                usersRepository.save(Users.builder().name("grow").password("password").build());
            }

            private final UserSignUpRequest request = UserSignUpRequest.builder()
                    .name("grow")
                    .password("password")
                    .build();

            @Test
            @DisplayName("Conflict 409을 응답한다")
            void it_response_409() throws Exception {
                final ResultActions resultActions = subject(request);

                resultActions.andExpect(status().isConflict())
                ;
            }
        }
    }

    @Nested
    @DisplayName("GET /api/v1/users/{name} 요청은")
    class Describe_GET {
        private ResultActions subject(String name) throws Exception {
            return mockMvc.perform(get("/api/v1/users/{name}", name));
        }

        @Nested
        @DisplayName("존재하는 사용자의 name이 주어지면")
        class Context_with_exist_user_name {
            private Users user;

            @BeforeEach
            void setUp() {
                user = usersRepository.save(Users.builder()
                        .name("grow")
                        .password("password")
                        .build());
            }

            @Test
            @DisplayName("200을 응답하고, 사용자의 id와 name을 리턴한다")
            void it_response_200_and_user_id_and_name() throws Exception {
                final ResultActions resultActions = subject(user.getName());

                resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("user_id").exists())
                        .andExpect(jsonPath("name").value(equalTo(user.getName())))
                ;
            }
        }

        @Nested
        @DisplayName("존재하지않는 사용자의 name이 주어지면")
        class Context_with_not_exist_user_name {
            private Users user;

            @BeforeEach
            void setUp() {
                user = usersRepository.save(Users.builder()
                        .name("grow")
                        .password("password")
                        .build());
                usersRepository.delete(user);
            }

            @Test
            @DisplayName("404를 응답한다")
            void it_response_404() throws Exception {
                final ResultActions resultActions = subject(user.getName());

                resultActions.andExpect(status().isNotFound())
                ;
            }
        }
    }
}
