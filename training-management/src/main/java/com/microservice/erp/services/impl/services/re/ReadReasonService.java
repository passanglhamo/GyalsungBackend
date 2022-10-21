package com.microservice.erp.services.impl.services.re;

import com.microservice.erp.domain.entities.Reason;
import com.microservice.erp.domain.repository.IReasonRepository;
import com.microservice.erp.services.iServices.re.IReadReasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadReasonService implements IReadReasonService {
    private final IReasonRepository repository;

    @Override
    public List<Reason> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Reason> findAllByStatus(Character status) {
        return repository.findAllByStatus(status);
    }

    @Override
    public Reason findById(Long id) {
        return repository.findById(id).get();
    }

}
