package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.dto.EnlistmentScheduleDto;
import com.microservice.erp.domain.repository.IEnlistmentScheduleRepository;
import com.microservice.erp.services.iServices.IUpdateEnlistmentScheduleService;
import com.microservice.erp.services.impl.mapper.EnlistmentScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpdateEnlistmentScheduleService implements IUpdateEnlistmentScheduleService {

    private final IEnlistmentScheduleRepository repository;

    @Override
    public ResponseEntity<?> updateEnlistmentSchedule(EnlistmentScheduleDto enlistmentScheduleDto) {
        if (repository.existsByToDateAfterAndIdNot(enlistmentScheduleDto.getFromDate(),enlistmentScheduleDto.getId())) {
            return new ResponseEntity<>("From Date already existed.", HttpStatus.ALREADY_REPORTED);
        }

        repository.findById(enlistmentScheduleDto.getId()).map(d -> {
            d.setFromDate(enlistmentScheduleDto.getFromDate());
            d.setToDate(enlistmentScheduleDto.getToDate());
            d.setStatus(enlistmentScheduleDto.getStatus());
            repository.save(d);
            return d;
        });

        return ResponseEntity.ok("Enlistment Date updated successfully.");
    }
}
