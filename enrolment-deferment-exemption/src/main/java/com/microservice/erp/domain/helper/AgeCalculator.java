package com.microservice.erp.domain.helper;

import java.util.Calendar;
import java.util.Date;

public class AgeCalculator {


    public static AgeDto getAge(Date birthDate, Date paramDate) {
        AgeDto ageDto = new AgeDto();
        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(birthDate);
        int birthYear = birthCalendar.get(Calendar.YEAR);
        int birthMonth = birthCalendar.get(Calendar.MONTH);
        int birthDay = birthCalendar.get(Calendar.DAY_OF_MONTH);

        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(paramDate);
        int currentYear = currentCalendar.get(Calendar.YEAR);
        int currentMonth = currentCalendar.get(Calendar.MONTH);
        int currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH);

        int ageYears = currentYear - birthYear;
        int ageMonths = currentMonth - birthMonth;
        int ageDays = currentDay - birthDay;

        if (currentMonth < birthMonth || (currentMonth == birthMonth && currentDay < birthDay)) {
            ageYears--;
            if (currentMonth < birthMonth) {
                ageMonths += 12 - birthMonth + currentMonth;
            } else {
                ageMonths = 12 - birthMonth + currentMonth;
            }
            if (currentDay < birthDay) {
                ageMonths--;
                int lastMonthMaxDays = getLastDayOfMonth(currentMonth - 1, currentYear);
                ageDays = lastMonthMaxDays - birthDay + currentDay;
            }
        }
//        System.out.println("Age: " + ageYears + " years, " + ageMonths + " months, and " + ageDays + " days");
        ageDto.setAge(ageYears);
        ageDto.setMonths(ageMonths);
        ageDto.setDays(ageDays);
        return ageDto;
    }

    private static int getLastDayOfMonth(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

}
