package com.microservice.erp.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.erp.domain.dto.NoticeDto;
import com.microservice.erp.services.iServices.ISendNotificationService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/notice")
public class SendNotificationController {
    private static Logger LOG = LoggerFactory.getLogger(SendNotificationController.class.getSimpleName());
    private ISendNotificationService iSendNotificationService;

    @GetMapping(value = "/checkNoticeAlreadySentOrNot")
    public ResponseEntity<?> checkNoticeAlreadySentOrNot(@RequestParam("year") String year,
                                                         @RequestParam("noticeConfigurationId") Long noticeConfigurationId) {
        return iSendNotificationService.checkNoticeAlreadySentOrNot(year, noticeConfigurationId);
    }

    @PostMapping(value = "/sendNotification")
    public ResponseEntity<?> sendNotification(@RequestHeader("Authorization") String authHeader, @RequestBody NoticeDto noticeDto) throws Exception {
        return iSendNotificationService.sendNotification(authHeader, noticeDto);
    }

    @KafkaListener(topics = {"${topic.enrolment}"}, concurrency = "1")
    public ResponseEntity<?> sendNotification(@Payload String message, Acknowledgment ack) throws Exception {

        LOG.info("ENROLMENT-TOPIC-QUEUE: Message received {} ", message);
        ObjectMapper mapper = new ObjectMapper();
        //TODO Mail Validation have to be done while Enrolment.... Not on mail sending.
        ResponseEntity responseEntity = ResponseEntity.ok("Test");
        /*NoticeDto noticeDto = mapper.readValue(message, NoticeDto.class);
        String authHeader = "";
        ResponseEntity responseEntity = iSendNotificationService.sendNotification(authHeader, noticeDto);*/
        ack.acknowledge();
        return responseEntity;
    }
}
