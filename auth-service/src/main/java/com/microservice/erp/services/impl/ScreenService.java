package com.microservice.erp.services.impl;

import com.microservice.erp.domain.entities.Screen;
import com.microservice.erp.domain.repositories.ScreenRepository;
import com.microservice.erp.services.definition.IScreenService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class ScreenService implements IScreenService {
    private final ScreenRepository saScreenRepository;

    public ScreenService(ScreenRepository saScreenRepository) {
        this.saScreenRepository = saScreenRepository;
    }

    @Override
    public ResponseEntity<?> saveScreen(Screen saScreen) {
        saScreenRepository.save(saScreen);
        return ResponseEntity.ok("Data saved successfully.");
    }

    @Override
    public List<Screen> getAllScreens() {
        return saScreenRepository.findAllByOrderByScreenNameAsc();
    }

    @Override
    public ResponseEntity<?> getScreenById(BigInteger id) {
        Screen saScreen = saScreenRepository.findById(id).get();
        return ResponseEntity.ok(saScreen);
    }
}

