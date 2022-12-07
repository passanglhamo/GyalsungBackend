package com.microservice.erp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventBus {
    String destinationEmail;
    String sentMailFrom;
    File attachmentFile;
    String messageBody;
    String subject;
    String mobileNo;

    public static EventBus withId(
            String destinationEmail,
            String sentMailFrom,
            File attachmentFile,
            String reasonId,
            String subject,
            String mobileNo) {
        return new EventBus(
                destinationEmail,
                sentMailFrom,
                attachmentFile,
                reasonId,
                subject,
                mobileNo);
    }
}
