package com.microservice.erp.services.impl;

import com.microservice.erp.domain.entities.DzongkhagTrainingPreAcaMapping;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.repositories.IDzongkhagTrainingAcaMappingRepository;
import com.microservice.erp.services.iServices.IReadDzongkhagTrainingAcaMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadDzongkhagTrainingMappingService implements IReadDzongkhagTrainingAcaMappingService {

    private final IDzongkhagTrainingAcaMappingRepository repository;

    @Override
    public List<DzongkhagTrainingPreAcaMapping> getAllDzongkhagTrainingList() {
        return repository.findAll();
    }

    @Override
    public ResponseEntity<?> getAllActiveTrainingsByDzongkhagId(Integer dzongkhagId) {

        return ResponseEntity.badRequest().body(new MessageResponse("Data not found"));

    }
}
