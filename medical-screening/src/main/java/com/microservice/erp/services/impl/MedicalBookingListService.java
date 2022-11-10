package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dao.MedicalBookingListDao;
import com.microservice.erp.domain.dto.MedicalBookingListDto;
import com.microservice.erp.domain.entities.HospitalScheduleDate;
import com.microservice.erp.domain.entities.MedicalSelfDeclaration;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.repositories.IHospitalScheduleDateRepository;
import com.microservice.erp.domain.repositories.IHospitalScheduleTimeRepository;
import com.microservice.erp.domain.repositories.IMedicalSelfDeclarationRepository;
import com.microservice.erp.services.iServices.IMedicalBookingListService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
@AllArgsConstructor
public class MedicalBookingListService implements IMedicalBookingListService {
    private final IHospitalScheduleDateRepository iHospitalScheduleDateRepository;
    private final IHospitalScheduleTimeRepository iHospitalScheduleTimeRepository;
    private final IMedicalSelfDeclarationRepository iMedicalSelfDeclarationRepository;
    private final MedicalBookingListDao medicalBookingListDao;

    @Override
    public ResponseEntity<?> getAllBookingDateByHospitalIdAndYear(BigInteger hospitalId, BigInteger year) {
        List<MedicalBookingListDto> medicalBookingListDtos = medicalBookingListDao.getAllBookingDateByHospitalIdAndYear(hospitalId, year);
        if (medicalBookingListDtos.size() > 0) {
            return ResponseEntity.ok(medicalBookingListDtos);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("No data available."));
        }
    }

    @Override
    public ResponseEntity<?> getTimeSlotsByScheduleDateId(BigInteger hospitalScheduleDateId) {
        List<MedicalBookingListDto> medicalBookingListDtos = medicalBookingListDao.getTimeSlotsByScheduleDateId(hospitalScheduleDateId);
        if (medicalBookingListDtos.size() > 0) {
            return ResponseEntity.ok(medicalBookingListDtos);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("No data available."));
        }
    }
}
