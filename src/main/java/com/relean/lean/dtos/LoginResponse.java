package com.relean.lean.dtos;

import lombok.Builder;

@Builder
public class LoginResponse {
    String email;
    String accessToken;
    String refreshToken;
    String status;

}
