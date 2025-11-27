package com.application.configuration;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.application.model.User;
import com.application.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Crée un compte administrateur par défaut si aucun n'existe.
 */
@Component
public class AdminBootstrap implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${app.admin.email:admin@gmail.com}")
    private String adminEmail;

    @Value("${app.admin.password:Admin@123}")
    private String adminPassword;

    public AdminBootstrap(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        User existing = userRepository.findByEmail(adminEmail);
        if (existing != null) {
            return; // admin déjà présent
        }

        User admin = new User();
        admin.setEmail(adminEmail);
        admin.setUsername("Admin");
        admin.setUserid(generateId());
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole("ADMIN");
        admin.setIsPremuim(false);
        admin.setGender("N/A");
        admin.setProfession("admin");
        admin.setAddress("system");

        userRepository.save(admin);
    }

    private String generateId() {
        return "ADM-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
