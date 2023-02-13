package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.NoticeDto;
import org.springframework.http.ResponseEntity;

import java.math.BigInteger;

public interface ISendNotificationService {
    ResponseEntity<?> sendNotification(String authHeader, NoticeDto noticeDto) throws Exception;

    ResponseEntity<?> checkNoticeAlreadySentOrNot(String year, BigInteger noticeConfigurationId);
}
