package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.entities.MedicalQuestionnaire;
import com.microservice.erp.services.iServices.ICreateMedicalQuestionService;
import com.microservice.erp.services.iServices.IReadMedicalQuestionService;
import lombok.AllArgsConstructor;
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

    @PostMapping
    public MedicalQuestionnaire saveMedicalQuestionnaire(@Valid @RequestBody MedicalQuestionnaire medicalQuestionnaire) {
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
}
