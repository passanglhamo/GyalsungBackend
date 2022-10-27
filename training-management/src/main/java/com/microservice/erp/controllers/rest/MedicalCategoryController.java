package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.entities.MedicalQuestionCategory;
import com.microservice.erp.services.iServices.ICreateMedicalCategoryService;
import com.microservice.erp.services.iServices.IReadMedicalCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/medicalQuestionCategories")
@AllArgsConstructor
public class MedicalCategoryController {
    private final ICreateMedicalCategoryService service;
    private final IReadMedicalCategoryService readService;

    @PostMapping
    public MedicalQuestionCategory saveMedicalCategory(@Valid @RequestBody MedicalQuestionCategory medicalQuestionCategory) {
        return service.saveMedicalCategory(medicalQuestionCategory);
    }

    @GetMapping
    public List<MedicalQuestionCategory> getAllMedicalCategoryList() {
        return readService.getAllMedicalCategoryList();
    }

    @GetMapping("/getAllMedicalCategoriesById")
    public MedicalQuestionCategory getAllMedicalCategoriesById(@RequestParam("id") Long id) {
        return readService.getAllMedicalCategoriesById(id);
    }

    @GetMapping("/getAllActiveMedicalCatList")
    public List<MedicalQuestionCategory> getAllActiveMedicalCatList() {
        return readService.getAllActiveMedicalCatList();
    }
}
