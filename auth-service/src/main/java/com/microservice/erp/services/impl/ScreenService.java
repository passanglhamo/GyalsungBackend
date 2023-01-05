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
    public ResponseEntity<?> getAllScreens() {
        List<Screen> saScreenGroups = saScreenRepository.findAllByOrderByScreenNameAsc();
        return ResponseEntity.ok(saScreenGroups);
    }

    @Override
    public ResponseEntity<?> getScreenById(BigInteger id) {
        Screen saScreen = saScreenRepository.findById(id).get();
        return ResponseEntity.ok(saScreen);
    }

    @Override
    public ResponseEntity<?> updateScreen(Screen saScreen) {
        saScreenRepository.findById(saScreen.getId()).ifPresent(d -> {
            d.setScreenGroupId(saScreen.getScreenGroupId());
            d.setScreenIconName(saScreen.getScreenIconName());
            d.setScreenName(saScreen.getScreenName());
            d.setScreenUrl(saScreen.getScreenUrl());
            saScreenRepository.save(d);

        });
        return ResponseEntity.ok("Data updated successfully.");
    }
}

