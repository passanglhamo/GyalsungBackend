package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.dto.EnlistmentScheduleDto;
import com.microservice.erp.domain.repository.IEnlistmentScheduleRepository;
import com.microservice.erp.services.iServices.ICreateEnlistmentScheduleService;
import com.microservice.erp.services.impl.mapper.EnlistmentScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;

@Service
@RequiredArgsConstructor
public class CreateEnlistmentScheduleService implements ICreateEnlistmentScheduleService {
    private final IEnlistmentScheduleRepository repository;
    private final EnlistmentScheduleMapper mapper;

    public ResponseEntity<?> saveEnlistmentSchedule(EnlistmentScheduleDto enlistmentScheduleDto) throws IOException, ParseException {

        if (repository.existsByToDateAfter(enlistmentScheduleDto.getFromDate())) {
            return new ResponseEntity<>("From Date already existed.", HttpStatus.ALREADY_REPORTED);
        }

        var enlistmentSchedule = repository.save(
                mapper.mapToEntity(enlistmentScheduleDto)
        );

        repository.save(enlistmentSchedule);

        return ResponseEntity.ok("Enlistment Date saved successfully.");
    }


}
