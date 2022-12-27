package com.microservice.erp.services.impl;

import com.microservice.erp.domain.entities.SaScreenGroup;
import com.microservice.erp.domain.repositories.SaScreenGroupRepository;
import com.microservice.erp.services.iServices.ISaScreenGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class SaScreenGroupService implements ISaScreenGroupService {
    private final SaScreenGroupRepository saScreenGroupRepository;

    public SaScreenGroupService(SaScreenGroupRepository saScreenGroupRepository) {
        this.saScreenGroupRepository = saScreenGroupRepository;
    }

    @Override
    public ResponseEntity<?> saveScreenGroup(SaScreenGroup saScreenGroup) {
        saScreenGroupRepository.save(saScreenGroup);
        return ResponseEntity.ok("Data saved successfully");
    }

    @Override
    public ResponseEntity<?> getAllScreens() {
        List<SaScreenGroup> saScreenGroups = saScreenGroupRepository.findAllByOrderByScreenGroupNameAsc();
        return ResponseEntity.ok(saScreenGroups);
    }

    @Override
    public ResponseEntity<?> getScreenById(BigInteger id) {
        SaScreenGroup saScreenGroup = saScreenGroupRepository.findById(id).get();
        return ResponseEntity.ok(saScreenGroup);

    }
}
