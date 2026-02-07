package com.relean.lean.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {
    private String fullName;
    private String email;
    private String password;
}
