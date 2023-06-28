package com.microservice.erp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventBusDto {
    String destinationEmail;
    String sentMailFrom;
    File attachmentFile;
    String messageBody;
    String subject;
    String mobileNo;
    List<String> destinationEmails;
    List<String> mobileNos;


    public static EventBusDto withId(
            String destinationEmail,
            String sentMailFrom,
            File attachmentFile,
            String reasonId,
            String subject,
            String mobileNo,
            List<String> destinationEmails,
            List<String> mobileNos) {
        return new EventBusDto(
                destinationEmail,
                sentMailFrom,
                attachmentFile,
                reasonId,
                subject,
                mobileNo,
                destinationEmails,
                mobileNos);
    }
}
