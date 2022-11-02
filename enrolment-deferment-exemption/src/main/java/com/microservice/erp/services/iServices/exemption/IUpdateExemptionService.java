package com.microservice.erp.services.iServices.exemption;

import com.microservice.erp.domain.dto.exemption.ExemptionDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

public interface IUpdateExemptionService {
    ResponseEntity<?> approveByIds(String authHeader,@Valid UpdateExemptionCommand command);

    ResponseEntity<?> rejectByIds(String authHeader,@Valid UpdateExemptionCommand command);

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class UpdateExemptionCommand {

        private String remarks;

        private List<Long> exemptionIds;
    }
}
