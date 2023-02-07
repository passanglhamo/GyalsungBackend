package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.NoticeDto;
import com.microservice.erp.services.iServices.ISendNotificationService;
import com.microservice.erp.services.impl.SpringSecurityAuditorAware;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/notice")
public class SendNotificationController {
    private ISendNotificationService iSendNotificationService;

    @GetMapping(value = "/checkNoticeAlreadySentOrNot")
    public ResponseEntity<?> checkNoticeAlreadySentOrNot(@RequestParam("year") String year,
                                                         @RequestParam("noticeConfigurationId") Long noticeConfigurationId) {
        return iSendNotificationService.checkNoticeAlreadySentOrNot(year, noticeConfigurationId);
    }

    @PostMapping(value = "/sendNotification")
    public ResponseEntity<?> sendNotification(@RequestHeader("Authorization") String authHeader,
                                              @RequestBody NoticeDto noticeDto,
                                              @RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws Exception {
        SpringSecurityAuditorAware.setToken(token);
        return iSendNotificationService.sendNotification(authHeader, noticeDto);
    }

}
