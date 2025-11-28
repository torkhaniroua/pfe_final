package com.application.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.application.model.User;
import com.application.model.Professor;
import com.application.model.request.ForgotPasswordRequest;
import com.application.services.EmailService;
import com.application.services.ProfessorService;
import com.application.services.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UserService userService;
    private final ProfessorService professorService;
    private final EmailService emailService;

    public AuthController(UserService userService, ProfessorService professorService, EmailService emailService) {
        this.userService = userService;
        this.professorService = professorService;
        this.emailService = emailService;
    }

    @PostMapping("/auth/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        String email = request.getEmail();
        if (!StringUtils.hasText(email)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));
        }

        // Generate a simple temporary password
        String tempPassword = UUID.randomUUID().toString().substring(0, 8);

        try {
            User user = userService.fetchUserByEmail(email);
            Professor prof = professorService.fetchProfessorByEmail(email);

            if (user != null) {
                userService.resetPassword(email, tempPassword);
            } else if (prof != null) {
                professorService.resetPassword(email, tempPassword);
            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Account not found"));
            }

            String body = "Hello,\n\n"
                    + "You requested a password reset. Here is your temporary password: " + tempPassword + "\n"
                    + "Please sign in and change it immediately.\n\n"
                    + "If you did not request this, please ignore this email.";

            emailService.sendPasswordResetEmail(email, "Password reset request", body);
            return ResponseEntity.ok(Map.of("message", "Temporary password sent to your email"));

        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }
}
