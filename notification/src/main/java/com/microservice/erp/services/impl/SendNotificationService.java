package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.NoticeDto;
import com.microservice.erp.services.iServices.ISendNotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SendNotificationService implements ISendNotificationService {
    @Override
    public ResponseEntity<?> sendNotification(NoticeDto noticeDto) {
        return null;
    }
}
