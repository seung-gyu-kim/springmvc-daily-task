package com.nhnacademy.springmvc.domain;

import lombok.Value;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Value
public class StudentRegisterRequest {
    @NotBlank
    private String name;

    @Email
    private String email;

    @Min(0) @Max(100)
    private int score;

    @NotBlank
    @Length(max = 200)
    private String comment;
}
