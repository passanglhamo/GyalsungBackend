package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.entities.Reason;
import com.microservice.erp.domain.repository.IReasonRepository;
import com.microservice.erp.services.iServices.IReadReasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadReasonService implements IReadReasonService {
    private final IReasonRepository repository;

    @Override
    public List<Reason> getAllReasonList() {
        return repository.findAll();
    }

    @Override
    public List<Reason> getAllReasonByStatus(String status) {
        return repository.findAllByStatus(status);
    }

    @Override
    public Reason getAllReasonById(Long id) {
        return repository.findById(id).get();
    }

}