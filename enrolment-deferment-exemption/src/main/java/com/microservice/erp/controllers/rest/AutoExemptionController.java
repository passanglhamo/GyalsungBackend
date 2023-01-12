package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.AutoExemptionDto;
import com.microservice.erp.services.iServices.IAutoExemptionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/autoExemption")
@AllArgsConstructor
public class AutoExemptionController {

    private final IAutoExemptionService iAutoExemptionService;

    @PostMapping(value = "/readFile")
    public ResponseEntity<?> readFile(@ModelAttribute AutoExemptionDto autoExemptionDto) throws Exception {
        return iAutoExemptionService.readFile(autoExemptionDto);
    }

    @PostMapping(value = "/uploadFile")
    public ResponseEntity<?> uploadFile(HttpServletRequest request, @ModelAttribute AutoExemptionDto autoExemptionDto) throws Exception {
        return iAutoExemptionService.uploadFile(request, autoExemptionDto);
    }
}
