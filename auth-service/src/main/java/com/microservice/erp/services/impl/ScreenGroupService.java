package com.microservice.erp.services.impl;

import com.microservice.erp.domain.entities.ScreenGroup;
import com.microservice.erp.domain.repositories.ScreenGroupRepository;
import com.microservice.erp.services.definition.IScreenGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class ScreenGroupService implements IScreenGroupService {
    private final ScreenGroupRepository saScreenGroupRepository;

    public ScreenGroupService(ScreenGroupRepository saScreenGroupRepository) {
        this.saScreenGroupRepository = saScreenGroupRepository;
    }

    @Override
    public ResponseEntity<?> saveScreenGroup(ScreenGroup saScreenGroup) {
        saScreenGroupRepository.save(saScreenGroup);
        return ResponseEntity.ok("Data saved successfully.");
    }

    @Override
    public ResponseEntity<?> getAllScreens() {
        List<ScreenGroup> saScreenGroups = saScreenGroupRepository.findAllByOrderByScreenGroupNameAsc();
        return ResponseEntity.ok(saScreenGroups);
    }

    @Override
    public ResponseEntity<?> getScreenById(BigInteger id) {
        ScreenGroup saScreenGroup = saScreenGroupRepository.findById(id).get();
        return ResponseEntity.ok(saScreenGroup);
    }

    @Override
    public ResponseEntity<?> updateScreenGroup(ScreenGroup saScreenGroup) {
        saScreenGroupRepository.findById(saScreenGroup.getId()).ifPresent(d -> {
            d.setScreenGroupName(saScreenGroup.getScreenGroupName());
            d.setScreenGroupIconName(saScreenGroup.getScreenGroupIconName());
            saScreenGroupRepository.save(d);

        });
        return ResponseEntity.ok("Data updated successfully.");
    }

}

