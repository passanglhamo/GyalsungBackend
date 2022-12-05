package com.microservice.erp.services.impl;

import com.google.gson.Gson;
import com.microservice.erp.domain.dto.MailSenderDto;
import com.microservice.erp.domain.helper.MailSender;
import com.microservice.erp.domain.helper.SmsSender;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService {

    @KafkaListener(topics = {"${topic.email}"}, concurrency = "1")
    public void sendEmail(@Payload String message, Acknowledgment ack) throws Exception {

        Gson gson = new Gson();
        MailSenderDto mailSenderDto = gson.fromJson(message, MailSenderDto.class);
        MailSender.sendMail(mailSenderDto.getDestinationEmail(), null, null, mailSenderDto.getMessageBody(), mailSenderDto.getSubject());

    }

    @KafkaListener(topics = {"${topic.sms}"}, concurrency = "1")
    public void sendSms(@Payload String message, Acknowledgment ack){

        Gson gson = new Gson();
        MailSenderDto mailSenderDto = gson.fromJson(message, MailSenderDto.class);
        SmsSender.sendSms(mailSenderDto.getMobileNo(), mailSenderDto.getMessageBody());

    }

}
