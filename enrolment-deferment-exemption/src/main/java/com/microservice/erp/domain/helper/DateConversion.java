package com.microservice.erp.domain.helper;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public class DateConversion {

    public static Date convertToDate(String time){
        Instant timestamp = Instant.parse(time);
        return new Date(timestamp.getEpochSecond() * 1000);
    }
}
