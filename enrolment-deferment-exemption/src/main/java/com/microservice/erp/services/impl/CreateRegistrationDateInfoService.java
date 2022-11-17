package com.microservice.erp.services.impl;

import com.microservice.erp.domain.entities.RegistrationDateInfo;
import com.microservice.erp.domain.repositories.IRegistrationDateInfoRepository;
import com.microservice.erp.services.iServices.ICreateRegistrationDateInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateRegistrationDateInfoService implements ICreateRegistrationDateInfoService {
    private final IRegistrationDateInfoRepository repository;


    @Override
    public RegistrationDateInfo saveRegistrationDateInfo(RegistrationDateInfo registrationDateInfo) {
        return repository.save(registrationDateInfo);
    }
}
