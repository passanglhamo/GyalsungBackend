package com.microservice.erp.controllers.rest;

import com.microservice.erp.services.iServices.ISendNotificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/sendNotification")
public class SendNotificationController {
    private ISendNotificationService iSendNotificationService;

}
