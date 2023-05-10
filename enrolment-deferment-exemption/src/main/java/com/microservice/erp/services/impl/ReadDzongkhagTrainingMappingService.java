package com.microservice.erp.services.impl;

import com.microservice.erp.domain.entities.DzongkhagTrainingMapping;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.repositories.IDzongkhagTrainingMappingRepository;
import com.microservice.erp.services.iServices.IReadDzongkhagTrainingMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadDzongkhagTrainingMappingService implements IReadDzongkhagTrainingMappingService {

    private final IDzongkhagTrainingMappingRepository repository;

    @Override
    public List<DzongkhagTrainingMapping> getAllDzongkhagTrainingList() {
        return repository.findAll();
    }



    @Override
    public List<DzongkhagTrainingMapping> getAllDzongkhagTrainingByStatus(String status) {
        return repository.findAllByStatus(status);
    }

    @Override
    public ResponseEntity<?> getAllActiveTrainingsByDzongkhagId(Integer dzongkhagId) {

        return ResponseEntity.badRequest().body(new MessageResponse("Data not found"));

    }
}
