package com.microservice.erp.services.impl;

import com.microservice.erp.domain.entities.Dzongkhag;
import com.microservice.erp.domain.entities.Geog;
import com.microservice.erp.domain.repository.IDzongkhagRepository;
import com.microservice.erp.domain.repository.IGeogRepository;
import com.microservice.erp.services.iServices.ICommonService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CommonService implements ICommonService {

    private IDzongkhagRepository dzongkhagRepository;
    private IGeogRepository geogRepository;

    @Override
    public ResponseEntity<?> getAllDzongkhags() {
        List<Dzongkhag> dzongkhags = dzongkhagRepository.findAllByOrderByDzongkhagNameAsc();
        return ResponseEntity.ok(dzongkhags);
    }

    @Override
    public ResponseEntity<?> getGeogByDzongkhagId(Integer dzongkhagId) {
        List<Geog> geogs = geogRepository.findByDzongkhagIdOrderByGeogNameAsc(dzongkhagId);
        return ResponseEntity.ok(geogs);
    }

    @Override
    public ResponseEntity<?> getGeogByGeogId(Integer geogId) {
        Geog geog = geogRepository.findByGeogId(geogId);
        return ResponseEntity.ok(geog);
    }

    @Override
    public ResponseEntity<?> getDzongkhagByDzongkhagId(Integer dzongkhagId) {
        Dzongkhag dzongkhag = dzongkhagRepository.findByDzongkhagId(dzongkhagId);
        return ResponseEntity.ok(dzongkhag);
    }
}
