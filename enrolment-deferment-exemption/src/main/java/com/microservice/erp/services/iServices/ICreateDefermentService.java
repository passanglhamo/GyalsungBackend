package com.microservice.erp.services.iServices;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;

/**
 * @author Passang Lhamo
 */

@Validated
public interface ICreateDefermentService {

    ResponseEntity<?> saveDeferment(HttpServletRequest request, @Valid CreateDefermentCommand command) throws Exception;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class CreateDefermentCommand {
        private BigInteger id;
        @NotNull(message = "User id cannot be null")
        private BigInteger userId;
        @NotNull(message = "Reason cannot be null")
        private BigInteger reasonId;
        private String approvalRemarks;
        @NotNull(message = "Till date cannot be null")
        private Date toDate;
        private Character status;
        private String remarks;
        private MultipartFile[] proofDocuments;
    }

}