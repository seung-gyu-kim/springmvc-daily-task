package com.nhnacademy.springmvc.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class Student {
    @Getter
    private long id;

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
