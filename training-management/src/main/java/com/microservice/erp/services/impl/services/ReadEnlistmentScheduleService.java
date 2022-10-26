package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.entities.EnlistmentSchedule;
import com.microservice.erp.domain.repository.IEnlistmentScheduleRepository;
import com.microservice.erp.services.iServices.IReadEnlistmentScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadEnlistmentScheduleService implements IReadEnlistmentScheduleService {

    private final IEnlistmentScheduleRepository repository;

    @Override
    public List<EnlistmentSchedule> findAll() {
        return repository.findAll();
    }


    @Override
    public EnlistmentSchedule findById(Long id) {
        return repository.findById(id).get();
    }
}
