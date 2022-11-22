package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.enrolment.EnrolmentDto;
import com.microservice.erp.domain.repositories.IEnrolmentInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface IEnrolmentInfoService {

    ResponseEntity<?> getRegistrationDateInfo();

    ResponseEntity<?> saveEnrolment(EnrolmentDto enrolmentDto);

}
