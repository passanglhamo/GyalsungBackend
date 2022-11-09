package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.*;
import com.microservice.erp.domain.entities.HospitalScheduleDate;
import com.microservice.erp.domain.entities.HospitalScheduleTime;
import com.microservice.erp.domain.entities.MedicalSelfDeclaration;
import com.microservice.erp.domain.repositories.IHospitalScheduleDateRepository;
import com.microservice.erp.domain.repositories.IHospitalScheduleTimeRepository;
import com.microservice.erp.domain.repositories.IMedicalSelfDeclarationRepository;
import com.microservice.erp.services.iServices.IMedicalBookingService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class MedicalBookingService implements IMedicalBookingService {
    private final IHospitalScheduleDateRepository iHospitalScheduleDateRepository;
    private final IHospitalScheduleTimeRepository iHospitalScheduleTimeRepository;
    private final IMedicalSelfDeclarationRepository iMedicalSelfDeclarationRepository;

    @Override
    public ResponseEntity<?> bookMedicalAppointment(MedicalBookingDto medicalBookingDto) {
        HospitalScheduleTime hospitalScheduleTimeDb = iHospitalScheduleTimeRepository.findById(medicalBookingDto.getScheduleTimeId()).get();
        HospitalScheduleTime hospitalScheduleTime = new ModelMapper().map(hospitalScheduleTimeDb, HospitalScheduleTime.class);
        hospitalScheduleTime.setBookedBy(2L);
        hospitalScheduleTime.setBookedDate(LocalDate.now());
        hospitalScheduleTime.setBookStatus('B');
        iHospitalScheduleTimeRepository.save(hospitalScheduleTime);

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

    @Override
    public ResponseEntity<?> getMedicalAppointmentDetail(String authHeader, Long userId) {
        ResponseDto responseDto = new ResponseDto();
        HospitalScheduleTime hospitalScheduleTime = iHospitalScheduleTimeRepository.findByBookedBy(userId);
        responseDto.setStartTime(hospitalScheduleTime.getStartTime());
        responseDto.setEndTime(hospitalScheduleTime.getEndTime());

        HospitalScheduleDate hospitalScheduleDate = iHospitalScheduleDateRepository.getMyBookingDate(userId);
        responseDto.setAppointmentDate(hospitalScheduleDate.getAppointmentDate());

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<String> request = new HttpEntity<>(headers);

        String hospitalUrl = "http://localhost:81/api/training/management/common/getHospitalById?hospitalId=" + hospitalScheduleDate.getHospitalId();
        ResponseEntity<HospitalDto> hospitalDtoResponse = restTemplate.exchange(hospitalUrl, HttpMethod.GET, request, HospitalDto.class);
        responseDto.setHospitalName(Objects.requireNonNull(hospitalDtoResponse.getBody()).getHospitalName());

        String dzongkhagUrl = "http://localhost:81/api/training/management/common/getHospitalMappingByHospitalId?hospitalId=" + hospitalScheduleDate.getHospitalId();
        ResponseEntity<DzongkhagDto> dzongkhagDtoResponse = restTemplate.exchange(dzongkhagUrl, HttpMethod.GET, request, DzongkhagDto.class);
        responseDto.setDzongkhagName(Objects.requireNonNull(dzongkhagDtoResponse.getBody()).getDzongkhagName());

        return ResponseEntity.ok(responseDto);
    }

    @Override
    public ResponseEntity<?> editMedicalAppointment(MedicalBookingDto medicalBookingDto) {
        HospitalScheduleTime hospitalScheduleTimeDbByUserId = iHospitalScheduleTimeRepository.findByBookedBy(medicalBookingDto.getUserId());
        HospitalScheduleTime hospitalScheduleTimeDb = iHospitalScheduleTimeRepository.findById(medicalBookingDto.getScheduleTimeId()).get();

        //todo: need to change book status of previous booking to A=Available
        HospitalScheduleTime hospitalScheduleTimeReset = new ModelMapper().map(hospitalScheduleTimeDbByUserId, HospitalScheduleTime.class);
        hospitalScheduleTimeReset.setBookedBy(null);
        hospitalScheduleTimeReset.setBookedDate(null);
        hospitalScheduleTimeReset.setBookStatus('A');
        iHospitalScheduleTimeRepository.save(hospitalScheduleTimeReset);

        HospitalScheduleTime hospitalScheduleTime = new ModelMapper().map(hospitalScheduleTimeDb, HospitalScheduleTime.class);
        hospitalScheduleTime.setBookedBy(2L);
        hospitalScheduleTime.setBookedDate(LocalDate.now());
        hospitalScheduleTime.setBookStatus('B');

        iHospitalScheduleTimeRepository.save(hospitalScheduleTime);


        return ResponseEntity.ok("Appointment edited successfully.");
    }

    @Override
    public ResponseEntity<?> getPreviousSelfDeclaration(String authHeader, Long userId) {
        List<MedicalSelfDeclaration> medicalSelfDeclarationList = iMedicalSelfDeclarationRepository.findByUserIdOrderByMedicalQuestionNameAsc(userId);
        return ResponseEntity.ok(medicalSelfDeclarationList);
    }

    @Override
    public ResponseEntity<?> resubmitSelfDeclaration(MedicalBookingDto medicalBookingDto) {
        iMedicalSelfDeclarationRepository.deleteAllByUserId(medicalBookingDto.getUserId());
        for (MedicalQuestionDto medicalQuestionDto : medicalBookingDto.getMedicalQuestionDtos()) {
            MedicalSelfDeclaration medicalSelfDeclaration = new MedicalSelfDeclaration();
            medicalSelfDeclaration.setUserId(2L);
            medicalSelfDeclaration.setMedicalQuestionnaireId(medicalQuestionDto.getMedicalQuestionId());
            medicalSelfDeclaration.setMedicalQuestionName(medicalQuestionDto.getMedicalQuestionName());
            medicalSelfDeclaration.setCheckStatus(medicalQuestionDto.getCheckStatus());
            iMedicalSelfDeclarationRepository.save(medicalSelfDeclaration);
        }
        return ResponseEntity.ok("Resubmitted successfully.");
    }

}
