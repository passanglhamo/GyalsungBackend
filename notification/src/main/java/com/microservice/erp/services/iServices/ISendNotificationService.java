package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.NoticeDto;
import org.springframework.http.ResponseEntity;

public interface ISendNotificationService {
    ResponseEntity<?> sendNotification(String authHeader, NoticeDto noticeDto);
}
