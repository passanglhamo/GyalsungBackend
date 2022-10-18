package com.microservice.erp.controllers.rest.fs;

import com.microservice.erp.domain.entities.FieldSpecialization;
import com.microservice.erp.services.iServices.fs.ICreateFieldSpecializationService;
import com.microservice.erp.services.iServices.fs.IReadFieldSpecializationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/fieldSpecializations")
@AllArgsConstructor
public class FieldSpecializationController {
    private final IReadFieldSpecializationService readService;
    private final ICreateFieldSpecializationService service;

    @PostMapping
    public FieldSpecialization insert(@Valid @RequestBody FieldSpecialization fieldSpecialization) {
        return service.add(fieldSpecialization);
    }

    @GetMapping(value = "/findAllByStatus")
    public ResponseEntity<?> findAllByStatus(@RequestParam("status") Character status) {
        return readService.findAllByStatus(status);
    }

    @GetMapping(value = "/findAllDefaultCourses")
    public ResponseEntity<?> findAllDefaultCourses() {
        return readService.findAllDefaultCourses();
    }

    @GetMapping(value = "/findAllMathRequiredCourses")
    public ResponseEntity<?> findAllMathRequiredCourses() {
        return readService.findAllMathRequiredCourses();
    }

    @GetMapping
    public List<FieldSpecialization> query() {
        return readService.findAll();
    }
}
