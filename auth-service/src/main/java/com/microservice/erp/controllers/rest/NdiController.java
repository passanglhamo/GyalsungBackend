package com.microservice.erp.controllers.rest;

import com.microservice.erp.services.definition.INdiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/ndi")
public class NdiController {
    private final INdiService iNdiService;

    public NdiController(INdiService iNdiService) {
        this.iNdiService = iNdiService;
    }

    @PostMapping("/proofRequest")
    public ResponseEntity<?> proofRequest() throws IOException, InterruptedException {
        return iNdiService.getProofRequest();
    }



}
