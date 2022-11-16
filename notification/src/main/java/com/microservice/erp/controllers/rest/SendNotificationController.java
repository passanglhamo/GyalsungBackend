package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.NoticeDto;
import com.microservice.erp.services.iServices.ISendNotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/sendNotification")
public class SendNotificationController {
    private ISendNotificationService iSendNotificationService;

    @PostMapping(value = "/sendNotification")
    public ResponseEntity<?> sendNotification(@RequestBody NoticeDto noticeDto) {
        return iSendNotificationService.sendNotification(noticeDto);
    }
}
