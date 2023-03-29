package com.microservice.erp.services.iServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dto.enrolment.EnrolmentDto;
import com.microservice.erp.domain.entities.EnrolmentInfo;
import com.microservice.erp.domain.repositories.IEnrolmentInfoRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;

public interface IEnrolmentInfoService {

    ResponseEntity<?> getRegistrationDateInfo();

    ResponseEntity<?> saveEnrolment(String authHeader, EnrolmentDto enrolmentDto) throws Exception;

    ResponseEntity<?> getEnrolmentListByYearAndCoursePreference(String authHeader, String year, Character applicationStatus, BigInteger courseId
            , Integer coursePreferenceNumber,String cid);

    ResponseEntity<?> allocateEnrolments(String authHeader, @Valid EnrolmentInfoCommand command) throws Exception;

    ResponseEntity<?> getEnrolmentListByYearCourseAndAcademy(String authHeader, String year, Integer trainingAcademyId, BigInteger courseId);

    ResponseEntity<?> changeTrainingAcademy(String authHeader, @Valid EnrolmentInfoCommand command) throws Exception;

    ResponseEntity<?> getEnrolmentValidation(BigInteger userId);

    ResponseEntity<?> getMyEnrolmentInfo(BigInteger userId);

    ResponseEntity<?> cancelEnrolments(String authHeader, EnrolmentInfoCommand command) throws JsonProcessingException;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class EnrolmentInfoCommand {

        private Integer trainingAcademyId;
        private BigInteger allocatedCourseId;

        private List<BigInteger> enrolmentIds;
    }
}
