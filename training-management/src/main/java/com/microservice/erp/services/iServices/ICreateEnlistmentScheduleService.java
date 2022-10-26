package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.EnlistmentScheduleDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.text.ParseException;

public interface ICreateEnlistmentScheduleService {
    ResponseEntity<?> save(EnlistmentScheduleDto enlistmentScheduleDto) throws IOException, ParseException;

}
