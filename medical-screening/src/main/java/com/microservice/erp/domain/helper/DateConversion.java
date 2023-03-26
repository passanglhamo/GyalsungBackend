package com.microservice.erp.domain.helper;

import org.springframework.stereotype.Component;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@Component
public class DateConversion {

    public static Date convertToDate(String time) {
        Instant timestamp = Instant.parse(time);
        return new Date(timestamp.getEpochSecond() * 1000);
    }

    public static Time extractTimeFromDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String timeString = dateFormat.format(date);
        return Time.valueOf(timeString);// extract LocalTime part
    }

    public static String changeDateFormat(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");

        return dateFormat.format(date);
    }

    public static String getTimeAMPMFormat(Time time){
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        return sdf.format(time);

    }
}
