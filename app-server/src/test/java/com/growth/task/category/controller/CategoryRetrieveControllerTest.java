package com.growth.task.category.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.growth.task.category.domain.Category;
import com.growth.task.category.repository.CategoryRepository;
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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("CategoryRetrieveController")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class CategoryRetrieveControllerTest {
    private static final String CATEGORY_NAME_STUDY = "공부";
    private static final String CATEGORY_NAME_READ = "독서";
    private static final String CATEGORY_NAME_WORK = "운동";
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

    @AfterEach
    void cleanUp() {
        categoryRepository.deleteAll();
    }

    @Nested
    @DisplayName("Category 조회 GET 요청은")
    class Describe_GET {
        private ResultActions subject() throws Exception {
            return mockMvc.perform(get("/api/v1/categories")
            );
        }

        @Nested
        @DisplayName("카페고리가 존재하면")
        class Context_when_exist_categories {

            @BeforeEach
            void prepare() {
                createCategory(CATEGORY_NAME_STUDY);
                createCategory(CATEGORY_NAME_READ);
                createCategory(CATEGORY_NAME_WORK);
            }

            @Test
            @DisplayName("200을 응답한다")
            void it_response_ok() throws Exception {
                final ResultActions resultActions = subject();

                resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("$.*", hasSize(3)))
                        .andExpect(jsonPath("$.[0].name").value(CATEGORY_NAME_STUDY))
                        .andExpect(jsonPath("$.[1].name").value(CATEGORY_NAME_READ))
                        .andExpect(jsonPath("$.[2].name").value(CATEGORY_NAME_WORK))
                ;
            }
        }

        @Nested
        @DisplayName("카페고리가 존재하지 않는다면")
        class Context_when_empty {

            @Test
            @DisplayName("빈 리스트와 200을 응답한다")
            void it_response_ok() throws Exception {
                final ResultActions resultActions = subject();

                resultActions.andExpect(status().isOk())
                        .andExpect(jsonPath("$.*").isEmpty())
                ;
            }
        }
    }

    private Category createCategory(String name) {
        return categoryRepository.save(Category.builder()
                .name(name)
                .build());
    }
}
