package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.entities.Reason;
import com.microservice.erp.domain.repository.IReasonRepository;
import com.microservice.erp.services.iServices.IUpdateReasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateReasonService implements IUpdateReasonService {
    private final IReasonRepository repository;

    @Override
    public ResponseEntity<?> updateReason(Reason reason) {
        repository.findById(reason.getId()).ifPresent(d -> {
            d.setReasonName(reason.getReasonName());
            d.setStatus(reason.getStatus());
            repository.save(d);
        });

        return ResponseEntity.ok("Reason updated successfully.");
    }
}
