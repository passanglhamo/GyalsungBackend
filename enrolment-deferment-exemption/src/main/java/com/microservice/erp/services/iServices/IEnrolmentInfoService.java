package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.enrolment.EnrolmentDto;
import com.microservice.erp.domain.repositories.IEnrolmentInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Rajib Kumer Ghosh
 */

public interface IEnrolmentInfoService {

    ResponseEntity<?> save(HttpServletRequest request, EnrolmentDto enrolmentDto);

    ResponseEntity<?> getActiveRegistrationInfo();
}
