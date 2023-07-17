package com.microservice.erp.services.impl;

import com.microservice.erp.domain.entities.RegistrationDateInfo;
import com.microservice.erp.domain.repositories.IRegistrationDateInfoRepository;
import com.microservice.erp.services.iServices.IReadRegistrationDateInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadRegistrationDateInfoService implements IReadRegistrationDateInfoService {

    private final IRegistrationDateInfoRepository repository;

    @Override
    public List<RegistrationDateInfo> getAllRegistrationDateList() {
        return repository.findAllByOrderByRegistrationYearDesc();
    }

    @Override
    public ResponseEntity<?> getRegistrationDateInfo() {
        RegistrationDateInfo registrationDateInfo = repository.findByStatus('A');
        return ResponseEntity.ok(registrationDateInfo);
    }
}
