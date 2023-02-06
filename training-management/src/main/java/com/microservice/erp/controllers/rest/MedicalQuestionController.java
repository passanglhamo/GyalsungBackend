package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.entities.MedicalQuestionnaire;
import com.microservice.erp.services.iServices.ICreateMedicalQuestionService;
import com.microservice.erp.services.iServices.IReadMedicalQuestionService;
import com.microservice.erp.services.iServices.IUpdateMedicalQuestionService;
import com.microservice.erp.services.impl.services.SpringSecurityAuditorAware;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/medicalQuestionnaires")
@AllArgsConstructor
public class MedicalQuestionController {

    private final ICreateMedicalQuestionService service;
    private final IReadMedicalQuestionService readService;
    private final IUpdateMedicalQuestionService updateService;

    @PostMapping
    public MedicalQuestionnaire saveMedicalQuestionnaire(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                         @Valid @RequestBody MedicalQuestionnaire medicalQuestionnaire) {
        SpringSecurityAuditorAware.setToken(token);
        return service.saveMedicalQuestionnaire(medicalQuestionnaire);
    }

    @GetMapping
    public List<MedicalQuestionnaire> getAllMedicalQuestionnaireList() {
        return readService.getAllMedicalQuestionnaireList();
    }

    @GetMapping("/getAllMedicalQuestionnaireById")
    public MedicalQuestionnaire getAllMedicalQuestionnaireById(@RequestParam("id") BigInteger id) {
        return readService.getAllMedicalQuestionnaireById(id);
    }

    @PutMapping("/updateMedicalQuestionnaire")
    public ResponseEntity<?> updateMedicalQuestionnaire(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                     @Valid @RequestBody MedicalQuestionnaire medicalQuestionnaire) {
        SpringSecurityAuditorAware.setToken(token);
        return updateService.updateMedicalQuestionnaire(medicalQuestionnaire);
    }
}
