package com.growth.task.category.dto;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryRequestTest {
    @ParameterizedTest
    @ValueSource(strings = {
            "a b",
            "a b ",
            "a      b",
            "a      b  ",
            " a b",
            " a b ",
            " a  b",
            " a  b ",
            " a  b  ",
            "  a   b ",
    })
    void remove_space(String text) {
        CategoryRequest actual = new CategoryRequest(text);
        assertThat(actual.getName()).isEqualTo("ab");
    }
}
