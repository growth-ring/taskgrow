package com.growth.task.category.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.growth.task.category.dto.CategoryRequest;
import com.growth.task.category.repository.CategoryRepository;
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

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("CategoryAddController")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class CategoryAddControllerTest {
    private static final String CATEGORY_NAME_STUDY = "공부";
    @Autowired
    private CategoryRepository categoryRepository;

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

    @Nested
    @DisplayName("Category 생성 POST 요청은")
    class Describe_POST {
        private ResultActions subject(CategoryRequest request) throws Exception {
            return mockMvc.perform(post("/api/v1/categories")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(APPLICATION_JSON)
                    .accept(APPLICATION_JSON)
            );
        }

        @Nested
        @DisplayName("카페고리명이 주어지면")
        class Context_with_category_name {
            private final CategoryRequest request = CategoryRequest.builder()
                    .name(CATEGORY_NAME_STUDY)
                    .build();

            @Test
            @DisplayName("201을 응답한다")
            void it_response_created() throws Exception {
                final ResultActions resultActions = subject(request);

                resultActions.andExpect(status().isCreated())
                        .andExpect(jsonPath("name").value(CATEGORY_NAME_STUDY))
                ;
            }
        }

        @Nested
        @DisplayName("카페고리명이 주어지지않으면")
        class Context_without_category_name {
            private final CategoryRequest request = CategoryRequest.builder()
                    .build();

            @Test
            @DisplayName("400을 응답한다")
            void it_response_bad_request() throws Exception {
                final ResultActions resultActions = subject(request);

                resultActions.andExpect(status().isBadRequest())
                ;
            }
        }

        @Nested
        @DisplayName("카페고리명에 빈 공백이 주어지지않으면")
        class Context_with_category_name_is_blank {
            private final CategoryRequest request = CategoryRequest.builder()
                    .name("")
                    .build();

            @Test
            @DisplayName("400을 응답한다")
            void it_response_bad_request() throws Exception {
                final ResultActions resultActions = subject(request);

                resultActions.andExpect(status().isBadRequest())
                ;
            }
        }
    }
}
