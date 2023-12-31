package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dao.ScreenDao;
import com.microservice.erp.domain.dto.MessageResponse;
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
    private final ScreenDao screenDao;

    public ScreenService(ScreenRepository saScreenRepository, ScreenDao screenDao) {
        this.saScreenRepository = saScreenRepository;
        this.screenDao = screenDao;
    }

    @Override
    public ResponseEntity<?> getLastScreenId() {
        Screen screen = saScreenRepository.findTop1ByOrderByScreenIdDesc();
        Integer lastScreenId = 1;
        if (screen != null) {
            lastScreenId = screen.getScreenId() + 1;
        }
        return ResponseEntity.ok(lastScreenId);
    }

    @Override
    public ResponseEntity<?> saveScreen(Screen saScreen) {
        Screen saScreenUrlDb = saScreenRepository.findByScreenUrl(saScreen.getScreenUrl());
        if (saScreenUrlDb != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Screen URL " + saScreen.getScreenUrl() + " already exist. Please try different one."));
        }
        Screen saScreenDb = saScreenRepository.findByScreenId(saScreen.getScreenId());
        if (saScreenDb != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Screen ID " + saScreen.getScreenId() + " already exist. Please try different one."));
        } else {
            saScreenRepository.save(saScreen);
            return ResponseEntity.ok().body(new MessageResponse("Data saved successfully."));
        }
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
            String isScreenUrlAlreadyExist = screenDao.isScreenUrlAlreadyExist(saScreen.getScreenUrl(), saScreen.getId());
            if (isScreenUrlAlreadyExist != null) {
                return;
            }
            String isScreenIdAlreadyExist = screenDao.isScreenIdAlreadyExist(saScreen.getScreenId(), saScreen.getId());
            if (isScreenIdAlreadyExist != null) {
                return;
            }
            d.setScreenGroupId(saScreen.getScreenGroupId());
            d.setScreenIconName(saScreen.getScreenIconName());
            d.setScreenId(saScreen.getScreenId());
            d.setScreenName(saScreen.getScreenName());
            d.setScreenUrl(saScreen.getScreenUrl());
            saScreenRepository.save(d);
        });
        return ResponseEntity.ok("Data updated successfully.");
    }

    @Override
    public ResponseEntity<?> removeById(BigInteger id) {
        saScreenRepository.deleteById(id);
        return  ResponseEntity.ok("Deleted successfully.");
    }
}

