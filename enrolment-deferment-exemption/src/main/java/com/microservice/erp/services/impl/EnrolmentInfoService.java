package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.enrolment.EnrolmentDto;
import com.microservice.erp.domain.entities.EnrolmentInfo;
import com.microservice.erp.domain.entities.RegistrationDateInfo;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.mapper.EnrolmentMapper;
import com.microservice.erp.domain.repositories.IEnrolmentCoursePreferenceRepository;
import com.microservice.erp.domain.repositories.IEnrolmentInfoRepository;
import com.microservice.erp.domain.repositories.IRegistrationDateInfoRepository;
import com.microservice.erp.services.iServices.IEnrolmentInfoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@AllArgsConstructor
public class EnrolmentInfoService implements IEnrolmentInfoService {

    private IEnrolmentInfoRepository iEnrolmentInfoRepository;
    private IEnrolmentCoursePreferenceRepository iEnrolmentCoursePreferenceRepository;

    private EnrolmentMapper enrolmentMapper;
    private final IRegistrationDateInfoRepository iRegistrationDateInfoRepository;

    @Override
    public ResponseEntity<?> getRegistrationDateInfo() {
        RegistrationDateInfo registrationDateInfo = iRegistrationDateInfoRepository.findByStatus('A');
        return ResponseEntity.ok(registrationDateInfo);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<?> saveEnrolment(EnrolmentDto enrolmentDto) {
        EnrolmentInfo enrolmentInfoDb = iEnrolmentInfoRepository.findByUserId(enrolmentDto.getUserId());
        //to check already enrolled or not
        if (enrolmentInfoDb != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("You have already enrolled."));
        }
        RegistrationDateInfo registrationDateInfo = iRegistrationDateInfoRepository.findByStatus('A');
        enrolmentDto.setYear(registrationDateInfo.getRegistrationYear());
        enrolmentDto.setStatus('P');//P=Pending, D=Deferred, E=Exempted, A=Approved, which means training academy allocated
        enrolmentDto.setEnrolledOn(new Date());
        var enrolmentInfo = iEnrolmentInfoRepository.save(enrolmentMapper.mapToEntity(enrolmentDto));
        iEnrolmentInfoRepository.save(enrolmentInfo);
        return ResponseEntity.ok(new MessageResponse("Enrolled successfully."));
    }

    @Override
    public ResponseEntity<?> getEnrolmentListByYearAndCourseId(String authHeader, String year, String courseId) {
        return null;
    }
}
