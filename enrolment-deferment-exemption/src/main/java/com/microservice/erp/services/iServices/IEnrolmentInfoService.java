package com.microservice.erp.services.iServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dto.enrolment.EnrolmentDto;
import com.microservice.erp.domain.entities.EnrolmentInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public interface IEnrolmentInfoService {

    ResponseEntity<?> getRegistrationDateInfo();

    ResponseEntity<?> saveEnrolment(String authHeader, EnrolmentDto enrolmentDto) throws Exception;

    ResponseEntity<?> getEnrolmentListByYearAndCoursePreference(String authHeader, String year, Character applicationStatus, String cid, Character gender);

    ResponseEntity<?> allocateEnrolments(String authHeader, @Valid EnrolmentInfoCommand command) throws Exception;

    ResponseEntity<?> getEnrolmentListByYearCourseAndAcademy(String authHeader, String year, Integer trainingAcademyId);

    ResponseEntity<?> changeTrainingAcademy(String authHeader, @Valid EnrolmentInfoCommand command) throws Exception;

    ResponseEntity<?> getEnrolmentValidation(BigInteger userId);

    ResponseEntity<?> getMyEnrolmentInfo(BigInteger userId);

    ResponseEntity<?> cancelEnrolments(String authHeader, EnrolmentInfoCommand command) throws JsonProcessingException;

    List<EnrolmentInfo> getEnrolmentListByYearAndAcademy(String authHeader, String year, Integer trainingAcademyId);

    ResponseEntity<?> allocateUserToTrainingAca(String authHeader,AllocationCommand allocationCommand) throws IOException;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class EnrolmentInfoCommand {

        private Integer trainingAcademyId;
        private String year;
//        private BigInteger allocatedCourseId;
        private List<BigInteger> enrolmentIds;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class AllocationCommand {
        private String year;
        private Character gender;
    }
}
