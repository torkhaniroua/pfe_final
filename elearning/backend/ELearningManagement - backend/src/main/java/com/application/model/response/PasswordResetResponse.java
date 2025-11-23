package com.application.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordResetResponse {
    private String email;
    private String role;
    private String message;
}

