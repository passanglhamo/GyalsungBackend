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
        if (repository.existsByRegistrationYear(registrationDateInfo.getRegistrationYear())) {
            return new ResponseEntity<>("Registration date is already added for given year.", HttpStatus.ALREADY_REPORTED);
        }
        repository.save(registrationDateInfo);

        return ResponseEntity.ok("Registration Date saved successfully.");
    }
}
