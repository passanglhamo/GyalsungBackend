package com.microservice.erp.domain.dto;

import com.opencsv.bean.CsvDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicalConfigurationExcelDto {
    @CsvDate("d/M/yyyy")
    private Date appointmentDate;
    private Integer amSlots;
    private Integer pmSlots;
    private String hospitalName;

    public static MedicalConfigurationExcelDto withId(
            Date appointmentDate,
            Integer amSlots,
            Integer pmSlots,
            String hospitalName) {
        return new MedicalConfigurationExcelDto(
                appointmentDate,
                amSlots,
                pmSlots,
                hospitalName);
    }
}
