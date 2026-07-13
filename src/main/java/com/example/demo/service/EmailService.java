package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@gourmetbookings.com}")
    private String fromEmail;

    public void sendConfirmPasswordEmail(String toEmail, String fullName, String confirmLink) {
        String subject = "Confirm Your Password - TMA Tour Bookings";
        String body = String.format(
                "Hello %s,\n\n" +
                "An account has been created for you on TMA Tour Bookings.\n" +
                "Please click the link below to confirm your password and activate your account:\n\n" +
                "%s\n\n" +
                "This link will expire in 24 hours.\n\n" +
                "Best regards,\n" +
                "TMA Tour Bookings Team",
                fullName, confirmLink
        );

        log.info("---------------- EMAIL LOG (DEV) ----------------");
        log.info("To: {}", toEmail);
        log.info("Subject: {}", subject);
        log.info("Link: {}", confirmLink);
        log.info("Body:\n{}", body);
        log.info("-------------------------------------------------");

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            log.info("Email sent successfully to {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send email via SMTP to {}: {}", toEmail, e.getMessage());
        }
    }
}
