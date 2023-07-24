package com.microservice.erp.services.iServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dto.HospitalBookingDetailsDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import scala.Char;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;

public interface IHospitalBookingDetailsService {
    ResponseEntity<?> preBookHospitalAppointment(String authHeader, HospitalBookingDetailsDto hospitalBookingDetailsDto) throws JsonProcessingException;

    HospitalBookingDetailsDto getHospitalBookingDetailByUserId(String authHeader, BigInteger userId);

    ResponseEntity<?> bookHospitalAppointment(String authHeader, @Valid BookHospitalCommand command,Character approveSave) throws JsonProcessingException;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class BookHospitalCommand {
        @NotNull(message = "Id should not be null")
        private BigInteger hospitalBookingDetailId;
        private Date appointmentDate;
        private Integer hospitalId;
        private String status;
        private Character amPm;
        private BigInteger userId;
        private String hospitalName;

        public static BookHospitalCommand withId(
                BigInteger hospitalBookingDetailId,
                Date appointmentDate,
                Integer hospitalId,
                String status,
                Character amPm,
                BigInteger userId,
                String hospitalName) {
            return new BookHospitalCommand(
                    hospitalBookingDetailId,
                    appointmentDate,
                    hospitalId,
                    status,
                    amPm,
                    userId,
                    hospitalName
            );
        }


    }
}
