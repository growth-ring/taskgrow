package com.growth.task.user.domain;

import com.growth.task.config.TestQueryDslConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestQueryDslConfig.class)
@DataJpaTest
@DisplayName("UsersRepository")
class UsersRepositoryTest {
    private final static String TEST_NAME = "test name";
    private final static String PASSWORD = "password";
    private final static int THREE = 3;

    @Autowired
    private UsersRepository usersRepository;

    @AfterEach
    void cleanUp() {
        usersRepository.deleteAll();
    }

    @Nested
    @DisplayName("findAll 메소드는")
    class Describe_findAll {
        @Nested
        @DisplayName("users가 존재하면")
        class Context_with_users {
            List<Users> givenUserList = new ArrayList<>();

            @BeforeEach
            void prepare() {
                for (int i = 0; i < THREE; i++) {
                    givenUserList.add(getTestUser(TEST_NAME + i));
                }
            }

            @Test
            @DisplayName("존재하는 USER 리스트를 가져온다")
            void it_return_user_list() {
                List<Users> result = usersRepository.findAll();
                assertAll(() -> assertThat(result).hasSize(givenUserList.size()));
            }
        }
    }

    private Users getTestUser(String name) {
        return usersRepository.save(Users.builder().name(name).password(PASSWORD).build());
    }
}
