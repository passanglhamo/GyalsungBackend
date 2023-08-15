package com.microservice.erp.services.iServices;

import com.jcraft.jsch.SftpException;
import com.microservice.erp.domain.dto.DefermentDto;
import com.microservice.erp.domain.dto.DefermentListDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.List;

public interface IReadDefermentService {
    List<DefermentDto> getAllDefermentList(String authHeader);

    List<DefermentListDto> getDefermentListByDefermentYearReasonStatus(String authHeader, String defermentYear, BigInteger reasonId, Character status, Character gender, String cid, String caseNumber);

    ResponseEntity<?> getDefermentByUserId(BigInteger userId);

    ResponseEntity<?> getDefermentValidation(BigInteger userId);

    List<DefermentDto> getApprovedListByDefermentYearAndUserId(String authHeader, String defermentYear, BigInteger userId);

    ResponseEntity<?> getDefermentListByUserId(BigInteger userId);

    ResponseEntity<?> downloadFile(String url) throws IOException, SftpException, URISyntaxException;

    List<DefermentDto> getDefermentAuditListByDefermentId(String authHeader,BigInteger defermentId);
}
