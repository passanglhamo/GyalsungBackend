package com.microservice.erp.services;

import com.microservice.erp.domain.entities.RegistrationDateInfo;
import com.microservice.erp.domain.repositories.IRegistrationDateInfoRepository;
import com.microservice.erp.services.iServices.IReadRegistrationDateInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadRegistrationDateInfoService implements IReadRegistrationDateInfoService {

    private final IRegistrationDateInfoRepository repository;
    @Override
    public List<RegistrationDateInfo> getAllRegistrationDateList() {
        return repository.findAll();
    }
}
