package com.growth.task.user.domain;

import com.growth.task.global.domain.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import static jakarta.persistence.GenerationType.IDENTITY;

@Slf4j
@Getter
@Entity
public class Users extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(length = 32, unique = true, nullable = false)
    private String name;

    @Column(nullable = false, length = 128)
    private String password;

    @Builder
    public Users(Long userId, String name, String password) {
        this.userId = userId;
        this.name = name;
        this.password = password;
    }

    protected Users() {
    }

    public void modifyPassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public Boolean authenticate(String password, PasswordEncoder passwordEncoder) {
        log.info("password: " + password);
        log.info("this.password: " + this.password);
        return passwordEncoder.matches(password, this.password);
    }
}
