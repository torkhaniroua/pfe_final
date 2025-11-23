package com.application.model.request;

import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String email;
    private String role; // optional hint (user/professor)
}

