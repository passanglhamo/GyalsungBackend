package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.entities.NoticeConfiguration;
import com.microservice.erp.services.iServices.ICreateNoticeConfigurationService;
import com.microservice.erp.services.iServices.IReadNoticeConfigurationService;
import com.microservice.erp.services.impl.SpringSecurityAuditorAware;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/noticeConfigurations")
@AllArgsConstructor
public class NoticeConfigurationController {

    private final ICreateNoticeConfigurationService service;
    private final IReadNoticeConfigurationService readService;

    @PostMapping
    public NoticeConfiguration saveNoticeConfiguration(@Valid @RequestBody NoticeConfiguration noticeConfiguration,
                                                       @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        SpringSecurityAuditorAware.setToken(token);
        return service.saveNoticeConfiguration(noticeConfiguration);
    }

    @GetMapping
    public List<NoticeConfiguration> getAllNoticeConfigurationList() {
        return readService.getAllNoticeConfigurationList();
    }

    @GetMapping("/getAllNoticeConfigurationById")
    public NoticeConfiguration getAllNoticeConfigurationById(@RequestParam("id") BigInteger id) {
        return readService.getAllNoticeConfigurationById(id);
    }


}
