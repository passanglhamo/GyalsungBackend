package com.microservice.erp.services.impl;

import com.microservice.erp.domain.entities.PersonStatus;
import com.microservice.erp.domain.repositories.IPersonStatusRepository;
import com.microservice.erp.services.iServices.IPersonStatusService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PersonStatusService implements IPersonStatusService {

    private final IPersonStatusRepository repository;

    @Override
    public List<PersonStatus> getPersonStatus() {
        return repository.findAllByOrderByPersonStatusAsc();
    }

}
