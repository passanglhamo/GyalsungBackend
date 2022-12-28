package com.microservice.erp.services.impl;

import com.microservice.erp.domain.entities.SaScreen;
import com.microservice.erp.domain.entities.SaScreenGroup;
import com.microservice.erp.domain.repositories.SaScreenRepository;
import com.microservice.erp.services.iServices.ISaScreenService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class SaScreenService implements ISaScreenService {
    private final SaScreenRepository saScreenRepository;

    public SaScreenService(SaScreenRepository saScreenRepository) {
        this.saScreenRepository = saScreenRepository;
    }

    @Override
    public ResponseEntity<?> saveScreen(SaScreen saScreen) {
        saScreenRepository.save(saScreen);
        return ResponseEntity.ok("Data saved successfully.");
    }

    @Override
    public ResponseEntity<?> getAllScreens() {
        List<SaScreen> saScreenGroups = saScreenRepository.findAllByOrderByScreenNameAsc();
        return ResponseEntity.ok(saScreenGroups);
    }

    @Override
    public ResponseEntity<?> getScreenById(BigInteger id) {
        SaScreen saScreen = saScreenRepository.findById(id).get();
        return ResponseEntity.ok(saScreen);
    }
}
