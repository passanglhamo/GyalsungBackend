package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.entities.FieldSpecialization;
import com.microservice.erp.services.iServices.ICreateFieldSpecializationService;
import com.microservice.erp.services.iServices.IReadFieldSpecializationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/fieldSpecializations")
@AllArgsConstructor
public class FieldSpecializationController {
    private final ICreateFieldSpecializationService service;
    private final IReadFieldSpecializationService readService;

    @PostMapping
    public FieldSpecialization saveFieldSpec(@Valid @RequestBody FieldSpecialization fieldSpecialization) {
        return service.saveFieldSpec(fieldSpecialization);
    }

    @GetMapping(value = "/getAllFieldSpecByStatus")
    public ResponseEntity<?> getAllFieldSpecByStatus(@RequestParam("status") Character status) {
        return readService.getAllFieldSpecByStatus(status);
    }

    /**
     * this api is used by enrolment microservice to get name of course
     *
     * @param courseId -- BigInteger
     * @return -- ResponseEntity<?>
     */
    @GetMapping(value = "/getCourseByCourseId")
    public ResponseEntity<?> getCourseByCourseId(@RequestParam("courseId") BigInteger courseId) {
        return readService.getCourseByCourseId(courseId);
    }

    @GetMapping(value = "/getAllDefaultCourses")
    public ResponseEntity<?> getAllDefaultCourses() {
        return readService.getAllDefaultCourses();
    }

    @GetMapping(value = "/getAllMathRequiredCourses")
    public ResponseEntity<?> getAllMathRequiredCourses() {
        return readService.getAllMathRequiredCourses();
    }

    @GetMapping
    public List<FieldSpecialization> getAllFieldSpecList() {
        return readService.getAllFieldSpecList();
    }
}
