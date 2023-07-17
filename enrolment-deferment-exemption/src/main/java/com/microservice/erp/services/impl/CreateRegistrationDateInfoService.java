package com.microservice.erp.services.impl;

import com.microservice.erp.domain.entities.RegistrationDateInfo;
import com.microservice.erp.domain.repositories.IRegistrationDateInfoRepository;
import com.microservice.erp.services.iServices.ICreateRegistrationDateInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

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

        RegistrationDateInfo registrationDateInfoId = repository.findFirstByOrderByRegistrationDateIdDesc();
        BigInteger registrationDateId = registrationDateInfoId == null ? BigInteger.ONE : registrationDateInfoId.getRegistrationDateId().add(BigInteger.ONE);
        registrationDateInfo.setRegistrationDateId(registrationDateId);
        repository.save(registrationDateInfo);

        return ResponseEntity.ok("Registration Date saved successfully.");
    }
}
