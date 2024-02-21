package com.nhnacademy.springmvc.domain;

import lombok.Getter;

public class User {
    @Getter
    private final String id;

    @Getter
    private final String password;

    public static User create(String id, String password) {
        return new User(id, password);
    }

    private User(String id, String password) {
        this.id = id;
        this.password = password;
    }
}
