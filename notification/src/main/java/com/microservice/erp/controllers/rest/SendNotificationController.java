package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.NoticeDto;
import com.microservice.erp.services.iServices.ISendNotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/notice")
public class SendNotificationController {
    private ISendNotificationService iSendNotificationService;

    @PostMapping(value = "/sendNotification")
    public ResponseEntity<?> sendNotification(@RequestHeader("Authorization") String authHeader, @RequestBody NoticeDto noticeDto) {
        return iSendNotificationService.sendNotification(authHeader, noticeDto);
    }
}
