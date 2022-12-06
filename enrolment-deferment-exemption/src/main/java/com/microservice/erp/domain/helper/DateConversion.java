package com.microservice.erp.domain.helper;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Component
public class DateConversion {

    public static Date convertToDate(String time){
        Instant timestamp = Instant.parse(time);
        return new Date(timestamp.getEpochSecond() * 1000);
    }

    public static String getYearFromDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return String.valueOf(cal.get(Calendar.YEAR));

    }
}
