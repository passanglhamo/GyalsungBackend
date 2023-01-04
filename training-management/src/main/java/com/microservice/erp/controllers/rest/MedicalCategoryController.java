package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.entities.MedicalQuestionCategory;
import com.microservice.erp.services.iServices.ICreateMedicalCategoryService;
import com.microservice.erp.services.iServices.IReadMedicalCategoryService;
import com.microservice.erp.services.iServices.IUpdateMedicalCategoryService;
import com.microservice.erp.services.impl.services.SpringSecurityAuditorAware;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/medicalQuestionCategories")
@AllArgsConstructor
public class MedicalCategoryController {
    private final ICreateMedicalCategoryService service;
    private final IReadMedicalCategoryService readService;
    private final IUpdateMedicalCategoryService updateService;

    @PostMapping
    public MedicalQuestionCategory saveMedicalCategory(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                       @Valid @RequestBody MedicalQuestionCategory medicalQuestionCategory) {
        SpringSecurityAuditorAware.setToken(token);
        return service.saveMedicalCategory(medicalQuestionCategory);
    }

    @GetMapping
    public List<MedicalQuestionCategory> getAllMedicalCategoryList() {
        return readService.getAllMedicalCategoryList();
    }

    @GetMapping("/getAllMedicalCategoriesById")
    public MedicalQuestionCategory getAllMedicalCategoriesById(@RequestParam("id") BigInteger id) {
        return readService.getAllMedicalCategoriesById(id);
    }

    @GetMapping("/getAllActiveMedicalCatList")
    public List<MedicalQuestionCategory> getAllActiveMedicalCatList() {
        return readService.getAllActiveMedicalCatList();
    }

    @PutMapping("/updateMedicalCategories")
    public ResponseEntity<?> updateMedicalCategories(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                     @Valid @RequestBody MedicalQuestionCategory medicalQuestionCategory) {
        SpringSecurityAuditorAware.setToken(token);
        return updateService.updateMedicalCategories(medicalQuestionCategory);
    }
}
