package com.microservice.erp.domain.helper;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
@Component
public class DateUtil {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    public Date stringToDate(String dateInString) throws ParseException {
        return format.parse(dateInString);
    }
}
