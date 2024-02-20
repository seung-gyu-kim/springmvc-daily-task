package com.nhnacademy.springmvc.domain;

import lombok.Getter;
import lombok.Setter;

public class Student {
    @Getter
    private final long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private int score;

    @Getter
    @Setter
    private String comment;

    private Student(long id) {
        this.id = id;
    }

    public static Student create(long id) {
        return new Student(id);
    }
}
