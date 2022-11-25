package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.DefermentDto;
import com.microservice.erp.services.impl.ReadDefermentService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public interface IReadDefermentService {
    List<DefermentDto> getAllDefermentList(String authHeader);

    ResponseEntity<?> downloadFiles(BigInteger defermentId, HttpServletResponse response);

    ResponseEntity<?> getDefermentListByToDateStatus(@Valid ReadDefermentCommand command);

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class ReadDefermentCommand {
        private Date toDate;
        private Character status;
    }
}
