package com.microservice.erp.services.impl;

import com.microservice.erp.domain.entities.RegistrationDateInfo;
import com.microservice.erp.domain.helper.DateConversion;
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
        if (!(registrationDateInfo.getRegistrationYear().
                equals(DateConversion.getYearFromDate(registrationDateInfo.getFromDate())))) {
                return new ResponseEntity<>("From date should be same as selected year.", HttpStatus.ALREADY_REPORTED);
        }
        if (repository.existsByRegistrationYear(registrationDateInfo.getRegistrationYear())) {
            return new ResponseEntity<>("Registration date is already added for given year.", HttpStatus.ALREADY_REPORTED);
        }
        repository.save(registrationDateInfo);

        return ResponseEntity.ok("Registration Date saved successfully.");
    }
}
