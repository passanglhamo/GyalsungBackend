package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.AutomaticExemptionDto;
import com.microservice.erp.services.iServices.IAutomaticExemptionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/automaticExemption")
@AllArgsConstructor
public class AutomaticExemptionController {

    private final IAutomaticExemptionService iAutomaticExemptionService;

    @PostMapping(value = "/readFile")
    public ResponseEntity<?> readFile(@ModelAttribute AutomaticExemptionDto automaticExemptionDto) throws Exception {
        return iAutomaticExemptionService.readFile(automaticExemptionDto);
    }

    @PostMapping(value = "/uploadFile")
    public ResponseEntity<?> uploadFile(HttpServletRequest request, @ModelAttribute AutomaticExemptionDto automaticExemptionDto) throws Exception {
        return iAutomaticExemptionService.uploadFile(request, automaticExemptionDto);
    }
}
