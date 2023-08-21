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
import java.math.BigInteger;
import java.util.Date;


@Validated
public interface ICreateDefermentService {

    ResponseEntity<?> saveDeferment(HttpServletRequest token, @Valid CreateDefermentCommand command) throws Exception;

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
        @NotNull(message = "Year cannot be null")
        private String defermentYear;
        private Character gender;
        private String remarks;
        private MultipartFile[] proofDocuments;
        @NotNull(message = "Is medical Reason cannot be null")
        private Character isMedicalReason;
    }

}
