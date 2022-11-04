package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.MedicalBookingDto;
import com.microservice.erp.domain.dto.MedicalQuestionDto;
import com.microservice.erp.domain.entities.HospitalScheduleTime;
import com.microservice.erp.domain.entities.MedicalSelfDeclaration;
import com.microservice.erp.domain.repositories.IHospitalScheduleTimeRepository;
import com.microservice.erp.domain.repositories.IMedicalSelfDeclarationRepository;
import com.microservice.erp.services.iServices.IMedicalBookingService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

@Service
@AllArgsConstructor
public class MedicalBookingService implements IMedicalBookingService {
    private final IHospitalScheduleTimeRepository iHospitalScheduleTimeRepository;
    private final IMedicalSelfDeclarationRepository iMedicalSelfDeclarationRepository;

    @Override
    public ResponseEntity<?> bookMedicalAppointment(MedicalBookingDto medicalBookingDto) {
        HospitalScheduleTime hospitalScheduleTimeDb = iHospitalScheduleTimeRepository.findById(medicalBookingDto.getScheduleTimeId()).get();
        HospitalScheduleTime hospitalScheduleTime = new ModelMapper().map(hospitalScheduleTimeDb, HospitalScheduleTime.class);
//        hospitalScheduleTime.setBookedBy(2L);
//        hospitalScheduleTime.setBookedDate(LocalDate.now());
//        hospitalScheduleTime.setBookStatus('B');
//        iHospitalScheduleTimeRepository.save(hospitalScheduleTime);

        for (MedicalQuestionDto medicalQuestionDto : medicalBookingDto.getMedicalQuestionDtos()) {
            MedicalSelfDeclaration medicalSelfDeclaration = new MedicalSelfDeclaration();
            medicalSelfDeclaration.setUserId(2L);
            medicalSelfDeclaration.setMedicalQuestionnaireId(medicalQuestionDto.getMedicalQuestionId());
            medicalSelfDeclaration.setMedicalQuestionName(medicalQuestionDto.getMedicalQuestionName());
            medicalSelfDeclaration.setCheckStatus(medicalQuestionDto.getCheckStatus());
            iMedicalSelfDeclarationRepository.save(medicalSelfDeclaration);
        }
        return ResponseEntity.ok("Appointment booked successfully.");
    }
}
