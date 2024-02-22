package com.nhnacademy.springmvc.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class StudentRestRequest {
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
