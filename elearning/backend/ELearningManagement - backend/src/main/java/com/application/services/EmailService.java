package com.application.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.mail.from:}")
    private String fromAddress;

    @Value("${spring.mail.username:}")
    private String mailUsername;

    public void sendPasswordResetEmail(String to, String subject, String body) {
        sendEmail(to, subject, body);
    }

    public void sendAccountCreationEmail(String to, String name, String loginUrl) {
        String safeName = (name == null || name.isBlank()) ? "there" : name;
        String body = "Hello " + safeName + ",\n\n"
                + "Your eLearning account has been created successfully.\n"
                + "You can sign in using the link below:\n"
                + loginUrl + "\n\n"
                + "If you did not request this, please ignore this email or contact support.";
        sendEmail(to, "Your eLearning account is ready", body);
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        String sender = StringUtils.hasText(fromAddress) ? fromAddress : mailUsername;
        if (StringUtils.hasText(sender)) {
            message.setFrom(sender);
        }
        message.setSubject(subject);
        message.setText(body);
        try {
            mailSender.send(message);
            log.info("Sent email to {}", to);
        } catch (MailException ex) {
            log.error("Failed to send email to {}: {}", to, ex.getMessage());
        }
    }
}
