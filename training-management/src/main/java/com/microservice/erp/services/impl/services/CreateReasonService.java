package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.entities.Reason;
import com.microservice.erp.domain.repository.IReasonRepository;
import com.microservice.erp.services.iServices.ICreateReasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateReasonService implements ICreateReasonService {

    private final IReasonRepository repository;

    @Override
    public Reason saveReason(Reason reason) {
        return repository.save(reason);
    }
}
