package com.relean.lean.dtos;



import lombok.Data;

@Data
public class StudentRegisterRequestDto {
    private Long studentId;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String password;
}

