package com.growth.task.todo.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.growth.task.todo.config.StatusDeserializer;

@JsonDeserialize(using = StatusDeserializer.class)
public enum Status {
    READY("ready", "대기중"),
    DONE("done", "완료"),
    PROGRESS("progress", "진행중");

    private final String key;
    private final String title;

    Status(String key, String title) {
        this.key = key;
        this.title = title;
    }

    public static boolean isDone(Status status) {
        return DONE.equals(status);
    }

    public static boolean isReady(Status status) {
        return READY.equals(status);
    }

    public static boolean isProgress(Status status) {
        return PROGRESS.equals(status);
    }

    public static boolean isRemain(Status status) {
        return isReady(status) || isProgress(status);
    }
}
