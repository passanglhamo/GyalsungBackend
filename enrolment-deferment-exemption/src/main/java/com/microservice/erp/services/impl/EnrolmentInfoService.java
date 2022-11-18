package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.enrolment.EnrolmentDto;
import com.microservice.erp.domain.entities.RegistrationDateInfo;
import com.microservice.erp.domain.mapper.enrolment.EnrolmentMapper;
import com.microservice.erp.domain.repositories.IEnrolmentInfoRepository;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.repositories.IRegistrationDateInfoRepository;
import com.microservice.erp.services.iServices.IEnrolmentInfoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

/**
 * @author Rajib Kumer Ghosh
 */

@Service
@AllArgsConstructor
public class EnrolmentInfoService implements IEnrolmentInfoService {

    private IEnrolmentInfoRepository iEnrolmentInfoRepository;
    private final EnrolmentMapper enrolmentMapper;
    private final IRegistrationDateInfoRepository iRegistrationDateInfoRepository;

    @Override
    public ResponseEntity<?> getActiveRegistrationInfo() {
        RegistrationDateInfo registrationDateInfo = iRegistrationDateInfoRepository.findByStatus('A');
        return ResponseEntity.ok(registrationDateInfo);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<?> save(HttpServletRequest request, EnrolmentDto enrolmentDto) {

        var enrolmentInfo = iEnrolmentInfoRepository.save(enrolmentMapper.mapToEntity(request, enrolmentDto));

        iEnrolmentInfoRepository.save(enrolmentInfo);

        return ResponseEntity.ok(new MessageResponse("Enrolled successfully."));
    }

}
