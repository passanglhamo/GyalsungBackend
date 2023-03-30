package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.BookHospitalDto;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;

public interface IBookHospitalService {
    ResponseEntity<?> getHospitalBookingByUserId(BookHospitalDto bookHospitalDto);

    ResponseEntity<?> getPreviousBookingDetailByUserId(String authHeader, BookHospitalDto bookHospitalDto);

    ResponseEntity<?> bookHospital(String authHeader, BookHospitalDto bookHospitalDto);

    ResponseEntity<?> getAllBookingByHospitalIdAndYear(String authHeader, BigInteger year, Integer hospitalId);
}
