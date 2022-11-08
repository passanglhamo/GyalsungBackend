package com.microservice.erp.services.impl.enrolment;

import com.microservice.erp.domain.dto.enrolment.EnrolmentDto;
import com.microservice.erp.domain.mapper.enrolment.EnrolmentMapper;
import com.microservice.erp.domain.repositories.IEnrolmentInfoRepository;
import com.microservice.erp.domain.helper.MessageResponse;
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

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<?> save(HttpServletRequest request, EnrolmentDto enrolmentDto) {

        var enrolmentInfo = iEnrolmentInfoRepository.save(enrolmentMapper.mapToEntity(request, enrolmentDto));

        iEnrolmentInfoRepository.save(enrolmentInfo);

        return ResponseEntity.ok(new MessageResponse("Enrolled successfully."));
    }
}
