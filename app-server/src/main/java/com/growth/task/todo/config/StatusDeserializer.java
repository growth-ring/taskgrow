package com.growth.task.todo.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.growth.task.todo.enums.Status;

import java.io.IOException;

public class StatusDeserializer extends JsonDeserializer<Status> {
    @Override
    public Status deserialize(JsonParser p, DeserializationContext context) throws IOException {
        String value = p.getValueAsString();

        try {
            return Status.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("올바른 상태 값이 반드시 입력되어야 합니다.");
        }
    }
}
