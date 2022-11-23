package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.enrolment.EnrolmentDto;
import com.microservice.erp.domain.repositories.IEnrolmentInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;

public interface IEnrolmentInfoService {

    ResponseEntity<?> getRegistrationDateInfo();

    ResponseEntity<?> saveEnrolment(EnrolmentDto enrolmentDto);

    ResponseEntity<?> getEnrolmentListByYearAndCoursePreference(String authHeader, String year, BigInteger courseId
            , Integer coursePreferenceNumber);
}
