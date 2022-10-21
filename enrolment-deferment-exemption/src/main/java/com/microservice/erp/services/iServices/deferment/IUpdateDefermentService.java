package com.microservice.erp.services.iServices.deferment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.List;

public interface IUpdateDefermentService {
    ResponseEntity<?> approveByIds(@Valid UpdateDefermentCommand command);

    ResponseEntity<?> rejectByIds(@Valid UpdateDefermentCommand command);

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class UpdateDefermentCommand {

        private String remarks;

        private List<Long> defermentIds;
    }
}
