package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.entities.FieldSpecialization;
import com.microservice.erp.services.iServices.ICreateFieldSpecializationService;
import com.microservice.erp.services.iServices.IReadFieldSpecializationService;
import com.microservice.erp.services.iServices.IUpdateFieldSpecializationService;
import com.microservice.erp.services.impl.services.SpringSecurityAuditorAware;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    private final IUpdateFieldSpecializationService updateService;

    @PostMapping
    public FieldSpecialization saveFieldSpec(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @Valid @RequestBody FieldSpecialization fieldSpecialization) {
        SpringSecurityAuditorAware.setToken(token);
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

    @PutMapping("/updateCourse")
    public ResponseEntity<?> updateCourse(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                          @Valid @RequestBody FieldSpecialization fieldSpecialization) {
        SpringSecurityAuditorAware.setToken(token);
        return updateService.updateCourse(fieldSpecialization);
    }
}
