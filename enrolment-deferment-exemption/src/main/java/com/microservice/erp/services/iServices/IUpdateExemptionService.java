package com.microservice.erp.services.iServices;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;

public interface IUpdateExemptionService {
    ResponseEntity<?> approveByIds(String authHeader,@Valid UpdateExemptionCommand command);

    ResponseEntity<?> rejectByIds(String authHeader,@Valid UpdateExemptionCommand command);

    ResponseEntity<?> saveToDraft(String authHeader, UpdateExemptionCommand command);

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class UpdateExemptionCommand {

        private BigInteger userId;
        private String remarks;
        private Character status;
        private List<BigInteger> exemptionIds;
    }
}
