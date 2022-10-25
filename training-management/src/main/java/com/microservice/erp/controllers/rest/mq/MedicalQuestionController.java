package com.microservice.erp.controllers.rest.mq;

import com.microservice.erp.domain.entities.MedicalQuestionnaire;
import com.microservice.erp.services.iServices.mq.ICreateMedicalQuestionService;
import com.microservice.erp.services.iServices.mq.IReadMedicalQuestionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/medicalQuestionnaires")
@AllArgsConstructor
public class MedicalQuestionController {

    private final ICreateMedicalQuestionService service;
    private final IReadMedicalQuestionService readService;

    @PostMapping
    public MedicalQuestionnaire insert(@Valid @RequestBody MedicalQuestionnaire medicalQuestionnaire) {
        return service.add(medicalQuestionnaire);
    }

    @GetMapping
    public List<MedicalQuestionnaire> query() {
        return readService.findAll();
    }

    @GetMapping("/findById")
    public MedicalQuestionnaire getById(@RequestParam("id") Long id) {
        return readService.findById(id);
    }
}
