package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.AutoExemptionDto;
import com.microservice.erp.domain.entities.AutoExemption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public interface IAutoExemptionService {
    ResponseEntity<?> readFile(AutoExemptionDto autoExemptionDto);

    ResponseEntity<?> uploadFile(HttpServletRequest request, AutoExemptionDto autoExemptionDto) throws IOException;

    ResponseEntity<?> getUploadedFiles();

    ResponseEntity<?> deleteFile(BigInteger fileId);

    ResponseEntity<?> getExemptedList();

    ResponseEntity<?> update(AutoExemption autoExemption);

    ResponseEntity<?> deleteList(AutoExemptionCommand command);

    ResponseEntity<?> save(AutoExemption autoExemption);

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class AutoExemptionCommand {
        private List<BigInteger> ids;
    }
}
