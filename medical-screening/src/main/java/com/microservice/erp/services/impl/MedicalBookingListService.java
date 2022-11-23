package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dao.MedicalBookingListDao;
import com.microservice.erp.domain.dto.MedicalBookingListDto;
import com.microservice.erp.domain.dto.MedicalQuestionDto;
import com.microservice.erp.domain.dto.UserInfoDto;
import com.microservice.erp.domain.entities.HospitalScheduleDate;
import com.microservice.erp.domain.entities.MedicalSelfDeclaration;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.repositories.IHospitalScheduleDateRepository;
import com.microservice.erp.domain.repositories.IHospitalScheduleTimeRepository;
import com.microservice.erp.domain.repositories.IMedicalSelfDeclarationRepository;
import com.microservice.erp.services.iServices.IMedicalBookingListService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public ResponseEntity<?> getTimeSlotsByScheduleDateId(String authHeader, BigInteger hospitalScheduleDateId) {
        List<MedicalBookingListDto> medicalBookingListDtos = medicalBookingListDao.getTimeSlotsByScheduleDateId(hospitalScheduleDateId);
        List<MedicalBookingListDto> bookingListDos = new ArrayList<>();
        if (medicalBookingListDtos.size() > 0) {
            for (MedicalBookingListDto medicalBookingListDto : medicalBookingListDtos) {
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", authHeader);
                HttpEntity<String> request = new HttpEntity<>(headers);
                if (medicalBookingListDto.getBooked_by() != null) {
                    String url = "http://localhost:81/api/user/profile/userProfile/getProfileInfo?userId=" + medicalBookingListDto.getBooked_by();
                    ResponseEntity<UserInfoDto> userInfoDtoResponse = restTemplate.exchange(url, HttpMethod.GET, request, UserInfoDto.class);
                    medicalBookingListDto.setFullName(Objects.requireNonNull(userInfoDtoResponse.getBody()).getFullName());
                    bookingListDos.add(medicalBookingListDto);
                } else {
                    bookingListDos.add(medicalBookingListDto);
                }
            }
            return ResponseEntity.ok(bookingListDos);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("No data available."));
        }
    }

    @Override
    public ResponseEntity<?> getBookingDetail(String authHeader, BigInteger hospitalScheduleTimeId, BigInteger bookedById) {
        List<MedicalSelfDeclaration> medicalSelfDeclarationList = iMedicalSelfDeclarationRepository.findByUserIdOrderByMedicalQuestionNameAsc(bookedById);
        return ResponseEntity.ok(medicalSelfDeclarationList);
    }
}
