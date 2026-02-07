package com.relean.lean.dtos;

import lombok.Data;

@Data
public class TeacherRequestDto {
    private Long teacherId;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String password;
}
