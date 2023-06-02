package com.microservice.erp.services.impl;

import com.google.gson.Gson;
import com.microservice.erp.domain.dto.MailSenderDto;
import com.microservice.erp.domain.helper.MailSender;
import com.microservice.erp.domain.helper.SmsSender;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class MailSenderService {

    @KafkaListener(topics = {"${topic.email}"}, concurrency = "1")
    public void sendEmail(@Payload String message, Acknowledgment ack) throws Exception {

        Gson gson = new Gson();
        MailSenderDto mailSenderDto = gson.fromJson(message, MailSenderDto.class);
        if(!Objects.isNull(mailSenderDto.getDestinationEmail())){
            MailSender.sendMail(mailSenderDto.getDestinationEmail(), null, null, mailSenderDto.getMessageBody(), mailSenderDto.getSubject());
        }

        if(!Objects.isNull(mailSenderDto.getDestinationEmails())){
            mailSenderDto.getDestinationEmails().forEach(email->{
                try {
                    MailSender.sendMail(email, null, null, mailSenderDto.getMessageBody(), mailSenderDto.getSubject());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

    }

    @KafkaListener(topics = {"${topic.sms}"}, concurrency = "1")
    public void sendSms(@Payload String message, Acknowledgment ack){

        Gson gson = new Gson();
        MailSenderDto mailSenderDto = gson.fromJson(message, MailSenderDto.class);
        if(!Objects.isNull(mailSenderDto.getMobileNo())){
            SmsSender.sendSms(mailSenderDto.getMobileNo(), mailSenderDto.getMessageBody());
        }


        if(!Objects.isNull(mailSenderDto.getMobileNos())){
            mailSenderDto.getMobileNos().forEach(mobileNo->{
                try {
                    SmsSender.sendSms(mobileNo, mailSenderDto.getMessageBody());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

    }

}
