package com.microservice.erp.controllers.rest;

import com.microservice.erp.services.iServices.ICommonService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/common")
@AllArgsConstructor
public class CommonController {

    private ICommonService iCommonService;

    @GetMapping("/getAllDzongkhags")
    public ResponseEntity<?> allDzongkhags() {
        return iCommonService.getAllDzongkhags();
    }

    @GetMapping("/getGeogByDzongkhagId")
    public ResponseEntity<?> getGeogByDzongkhagId(@RequestParam("dzongkhagId") Integer dzongkhagId) {
        return iCommonService.getGeogByDzongkhagId(dzongkhagId);
    }

    @GetMapping("/getGeogByGeogId")
    public ResponseEntity<?> getGeogByGeogId(@RequestParam("geogId") Integer geogId) {
        return iCommonService.getGeogByGeogId(geogId);
    }

    @GetMapping("/getDzongkhagByDzongkhagId")
    public ResponseEntity<?> getDzongkhagByDzongkhagId(@RequestParam("dzongkhagId") Integer dzongkhagId) {
        return iCommonService.getDzongkhagByDzongkhagId(dzongkhagId);
    }

    @GetMapping("/getAllHospitals")
    public ResponseEntity<?> allHospitals() {
        return iCommonService.getAllHospitals();
    }
}
