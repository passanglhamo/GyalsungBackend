package com.microservice.erp.controllers.rest.mc;

import com.microservice.erp.domain.entities.MedicalQuestionCategory;
import com.microservice.erp.services.iServices.mc.ICreateMedicalCategoryService;
import com.microservice.erp.services.iServices.mc.IReadMedicalCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/medicalQuestionCategories")
@AllArgsConstructor
public class MedicalCategoryController {

    private final IReadMedicalCategoryService readService;
    private final ICreateMedicalCategoryService service;

    @PostMapping
    public MedicalQuestionCategory insert(@Valid @RequestBody MedicalQuestionCategory medicalQuestionCategory) {
        return service.add(medicalQuestionCategory);
    }

    @GetMapping
    public List<MedicalQuestionCategory> query() {
        return readService.findAll();
    }

    @GetMapping("/findById")
    public MedicalQuestionCategory getById(@RequestParam("id") Long id) {
        return readService.findById(id);
    }
}
