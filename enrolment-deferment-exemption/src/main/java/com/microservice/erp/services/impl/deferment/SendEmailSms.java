package com.microservice.erp.services.impl.deferment;

import com.microservice.erp.services.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendEmailSms {
    private final EmailSenderService emailSenderService;

    public void sendEmail(String email, String message) {
        emailSenderService.sendSimpleEmail(email, "Email verification", message);
    }
}
