package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.dao.TrainingDateDao;
import com.microservice.erp.domain.entities.TrainingDate;
import com.microservice.erp.domain.helper.LocalDateTimeGenerator;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.repository.ITrainingDateRepository;
import com.microservice.erp.services.iServices.ITrainingDateService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;

@Service
@AllArgsConstructor
public class TrainingDateService implements ITrainingDateService {
    private final ITrainingDateRepository iTrainingDateRepository;
    private  final TrainingDateDao trainingDateDao;

    @Override
    public ResponseEntity<?> saveTrainingDate(BigInteger userId, TrainingDate trainingDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(trainingDate.getTrainingDate());
        Integer year = calendar.get(Calendar.YEAR);
        Character validation = trainingDateDao.findByYear(year);
        if (validation != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Date already exist for the year " + year));
        }

        TrainingDate trainingDateDb = iTrainingDateRepository.findFirstByOrderByTrainingDateIdDesc();
        BigInteger trainingDateId = trainingDateDb == null ? BigInteger.ONE : trainingDateDb.getTrainingDateId().add(BigInteger.ONE);
        trainingDate.setTrainingDateId(trainingDateId);
        trainingDate.setCreatedBy(userId);
        trainingDate.setCreatedDate(LocalDateTimeGenerator.getLocalDateTime());
        iTrainingDateRepository.save(trainingDate);
        return ResponseEntity.ok(new MessageResponse("Data saved successfully."));
    }

    @Override
    public ResponseEntity<?> updateTrainingDate(BigInteger userId, TrainingDate trainingDate) {

        TrainingDate trainingDateDb = iTrainingDateRepository.findByTrainingDateId(trainingDate.getTrainingDateId());
        TrainingDate trainingDateUpdate = new ModelMapper().map(trainingDateDb, TrainingDate.class);
        trainingDateUpdate.setTrainingDate(trainingDate.getTrainingDate());
        trainingDateUpdate.setStatus(trainingDate.getStatus());
        trainingDate.setTrainingDateId(trainingDate.getTrainingDateId());
        trainingDate.setUpdatedBy(userId);
        trainingDate.setUpdatedDate(LocalDateTimeGenerator.getLocalDateTime());
        iTrainingDateRepository.save(trainingDate);
        return ResponseEntity.ok(new MessageResponse("Data updated successfully."));
    }

    @Override
    public ResponseEntity<?> getTrainingDate() {
        List<TrainingDate> trainingDates = iTrainingDateRepository.findAll();
        if (trainingDates.size() > 0) {
            return ResponseEntity.ok(trainingDates);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Data not found."));
        }
    }

    @Override
    public ResponseEntity<?> getActiveTrainingDate() {
        TrainingDate trainingDate = iTrainingDateRepository.findByStatus('A');
        if (trainingDate != null) {
            return ResponseEntity.ok(trainingDate);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Data not found."));
        }
    }

}
