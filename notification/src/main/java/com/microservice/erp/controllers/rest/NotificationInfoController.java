/*
package com.microservice.erp.controllers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.erp.services.iServices.INotificationInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

*/
/**
 * @author Rajib Kumer Ghosh
 *
 *//*


@RestController
@RequestMapping("/notify")
public class NotificationInfoController {

    private INotificationInfoService service;
    private ObjectMapper mapper;

    public NotificationInfoController(INotificationInfoService service, ObjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> getHello() throws JsonProcessingException {
        String msg = service.getHello();
        return ResponseEntity.ok(mapper.writeValueAsString(msg));
    }
}
*/
