package com.microservice.erp.domain.helper;

import java.util.Calendar;
import java.util.Date;

public class LocalDateTimeGenerator {
    public static Date getLocalDateTime() {
        return Calendar.getInstance().getTime();
    }
}
