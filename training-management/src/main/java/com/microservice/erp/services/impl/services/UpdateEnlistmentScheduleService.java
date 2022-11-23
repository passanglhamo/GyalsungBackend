package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.dto.EnlistmentScheduleDto;
import com.microservice.erp.domain.entities.EnlistmentSchedule;
import com.microservice.erp.domain.repository.IEnlistmentScheduleRepository;
import com.microservice.erp.services.iServices.IUpdateEnlistmentScheduleService;
import com.microservice.erp.services.impl.mapper.EnlistmentScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpdateEnlistmentScheduleService implements IUpdateEnlistmentScheduleService {

    private final IEnlistmentScheduleRepository repository;
    private final EnlistmentScheduleMapper mapper;

    @Override
    public ResponseEntity<?> updateEnlistmentSchedule(EnlistmentScheduleDto enlistmentScheduleDto) {
        repository.findById(enlistmentScheduleDto.getId()).stream().map(d -> {
            d.setFromDate(enlistmentScheduleDto.getFromDate());
            d.setToDate(enlistmentScheduleDto.getToDate());
            d.setStatus(enlistmentScheduleDto.getStatus());
            repository.save(d);
            return d;
        }).collect(Collectors.toUnmodifiableList());


//        EnlistmentSchedule enlistmentSchedule = new ModelMapper().map(enlistmentScheduleDto, EnlistmentSchedule.class);
//
//        repository.save(enlistmentSchedule);

        return ResponseEntity.ok("Enlistment Date updated successfully.");
    }
}
