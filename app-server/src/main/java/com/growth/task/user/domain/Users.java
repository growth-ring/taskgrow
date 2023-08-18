package com.growth.task.user.domain;

import com.growth.task.commons.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
public class Users extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(columnDefinition = "varchar(32)", unique = true, nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "varchar(32)")
    private String password;

    @Builder
    public Users(Long userId, String name, String password) {
        this.userId = userId;
        this.name = name;
        this.password = password;
    }

    protected Users() {
    }
}
