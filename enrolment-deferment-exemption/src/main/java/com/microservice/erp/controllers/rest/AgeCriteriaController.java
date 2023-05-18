package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.entities.AgeCriteria;
import com.microservice.erp.services.iServices.IAgeCriteriaService;
import com.microservice.erp.services.impl.SpringSecurityAuditorAware;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ageCriteria")
@AllArgsConstructor
public class AgeCriteriaController {
    private final IAgeCriteriaService iAgeCriteriaService;

    @PostMapping(value = "/saveAgeCriteria")
    public ResponseEntity<?> saveAgeCriteria(@RequestBody AgeCriteria ageCriteria,
                                             @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        SpringSecurityAuditorAware.setToken(token);
        return iAgeCriteriaService.saveAgeCriteria(ageCriteria);
    }

    @GetMapping(value = "/getAgeCriteria")
    public ResponseEntity<?> getAgeCriteria() {
        return iAgeCriteriaService.getAgeCriteria();
    }

}
