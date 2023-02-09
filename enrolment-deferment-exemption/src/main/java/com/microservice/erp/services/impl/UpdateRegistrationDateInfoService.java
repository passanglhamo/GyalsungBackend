package com.microservice.erp.services.impl;

import com.microservice.erp.domain.entities.RegistrationDateInfo;
import com.microservice.erp.domain.helper.DateConversion;
import com.microservice.erp.domain.repositories.IRegistrationDateInfoRepository;
import com.microservice.erp.services.iServices.IUpdateRegistrationDateInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateRegistrationDateInfoService implements IUpdateRegistrationDateInfoService {
    private final IRegistrationDateInfoRepository repository;

    @Override
    public ResponseEntity<?> updateRegistrationDateInfo(RegistrationDateInfo registrationDateInfo) {

        //todo remove static code
        if (repository.existsByStatusAndIdNot('A', registrationDateInfo.getId())) {
            return new ResponseEntity<>("There should be only one active registration year.", HttpStatus.ALREADY_REPORTED);
        }
        if (repository.existsByRegistrationYearAndIdNot(registrationDateInfo.getRegistrationYear(), registrationDateInfo.getId())) {
            return new ResponseEntity<>("Registration date is already added for given year.", HttpStatus.ALREADY_REPORTED);
        }
        repository.findById(registrationDateInfo.getId()).map(d -> {
            d.setRegistrationYear(registrationDateInfo.getRegistrationYear());
            d.setFromDate(registrationDateInfo.getFromDate());
            d.setToDate(registrationDateInfo.getToDate());
            d.setStatus(registrationDateInfo.getStatus());
            repository.save(d);
            return d;
        });

        return ResponseEntity.ok("Registration Date updated successfully.");

    }
}
