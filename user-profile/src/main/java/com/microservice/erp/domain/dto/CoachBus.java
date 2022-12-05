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
public class CoachBus {
    String destinationEmail;
    String sentMailFrom;
    File attachmentFile;
    String messageBody;
    String subject;
    String mobileNo;

    public static CoachBus withId(
            String destinationEmail,
            String sentMailFrom,
            File attachmentFile,
            String reasonId,
            String subject,
            String mobileNo) {
        return new CoachBus(
                destinationEmail,
                sentMailFrom,
                attachmentFile,
                reasonId,
                subject,
                mobileNo);
    }
}
