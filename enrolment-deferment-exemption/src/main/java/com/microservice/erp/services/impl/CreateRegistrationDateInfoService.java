package com.microservice.erp.services.impl;

import com.microservice.erp.domain.entities.RegistrationDateInfo;
import com.microservice.erp.domain.repositories.IRegistrationDateInfoRepository;
import com.microservice.erp.services.iServices.ICreateRegistrationDateInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateRegistrationDateInfoService implements ICreateRegistrationDateInfoService {
    private final IRegistrationDateInfoRepository repository;


    @Override
    public ResponseEntity<?> saveRegistrationDateInfo(RegistrationDateInfo registrationDateInfo) {
        //todo remove static code
        if (repository.existsByStatus('A')) {
            return new ResponseEntity<>("There should be only one active registration year.", HttpStatus.ALREADY_REPORTED);
        }

        repository.save(registrationDateInfo);

        return ResponseEntity.ok("Registration Date saved successfully.");
    }
}
