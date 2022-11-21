package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.enrolment.EnrolmentCourses;
import com.microservice.erp.domain.dto.enrolment.EnrolmentDto;
import com.microservice.erp.domain.entities.EnrolmentCoursePreference;
import com.microservice.erp.domain.entities.EnrolmentInfo;
import com.microservice.erp.domain.entities.RegistrationDateInfo;
import com.microservice.erp.domain.repositories.IEnrolmentCoursePreferenceRepository;
import com.microservice.erp.domain.repositories.IEnrolmentInfoRepository;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.repositories.IRegistrationDateInfoRepository;
import com.microservice.erp.services.iServices.IEnrolmentInfoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class EnrolmentInfoService implements IEnrolmentInfoService {

    private IEnrolmentInfoRepository iEnrolmentInfoRepository;
    private IEnrolmentCoursePreferenceRepository iEnrolmentCoursePreferenceRepository;
    private final IRegistrationDateInfoRepository iRegistrationDateInfoRepository;

    @Override
    public ResponseEntity<?> getRegistrationDateInfo() {
        RegistrationDateInfo registrationDateInfo = iRegistrationDateInfoRepository.findByStatus('A');
        return ResponseEntity.ok(registrationDateInfo);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<?> saveEnrolment(HttpServletRequest request, EnrolmentDto enrolmentDto) {
        EnrolmentInfo enrolmentInfo = new EnrolmentInfo();
        enrolmentInfo.setUserId(enrolmentDto.getUserId());
        enrolmentInfo.setStatus('P');//P=Pending, D=Deferred, E=Exempted, A=Approved, which means training academy allocated
        enrolmentInfo.setEnrolledOn(new Date());

         Set<EnrolmentCoursePreference> enrolmentCoursePreferences = new HashSet<>();

        for (EnrolmentCourses enrolmentCourses :enrolmentDto.getEnrolmentCourses()) {
            EnrolmentCoursePreference enrolmentCoursePreference = new EnrolmentCoursePreference();
            enrolmentCoursePreference.setCourseId(enrolmentCourses.getCourseId());
            enrolmentCoursePreference.setPreferenceNumber(enrolmentCourses.getPreferenceNumber());
            enrolmentCoursePreferences.add(enrolmentCoursePreference);
        }
        enrolmentInfo.setEnrolmentCoursePreferences(enrolmentCoursePreferences);
        iEnrolmentInfoRepository.save(enrolmentInfo);


        return ResponseEntity.ok(new MessageResponse("Enrolled successfully."));
    }

}
