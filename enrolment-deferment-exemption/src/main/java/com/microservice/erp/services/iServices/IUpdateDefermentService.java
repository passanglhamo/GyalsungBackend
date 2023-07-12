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
    ResponseEntity<?> approveByIds(String authHeader, @Valid UpdateDefermentCommand command);

    ResponseEntity<?> rejectByIds(String authHeader, @Valid UpdateDefermentCommand command);

    ResponseEntity<?> saveToDraft(String authHeader, @Valid UpdateDefermentCommand command);

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
}
