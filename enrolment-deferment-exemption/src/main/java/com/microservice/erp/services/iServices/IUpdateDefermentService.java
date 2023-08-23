package com.microservice.erp.services.iServices;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;

public interface IUpdateDefermentService {
    ResponseEntity<?> reviewRevertById(String authHeader, @Valid ReviewDefermentCommand command) throws Exception;

    ResponseEntity<?> approveRejectById(String authHeader, @Valid ReviewDefermentCommand command) throws Exception;

    ResponseEntity<?> mailSendToApplicant(String authHeader, ReviewDefermentCommand command);

    ResponseEntity<?> saveDraftById(String authHeader, ReviewDefermentCommand command);

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class UpdateDefermentCommand {
        private BigInteger userId;
        private String remarks;
        private Character status;
        private List<BigInteger> defermentIds;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class ReviewDefermentCommand {
        private BigInteger userId;
        private String reviewRemarks;
        private Character status;
        private BigInteger defermentId;
        private String studentName;
    }



}
