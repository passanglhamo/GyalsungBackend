package com.microservice.erp.controllers.rest;

import com.infoworks.lab.jjwt.JWTPayload;
import com.infoworks.lab.jjwt.TokenValidator;
import com.microservice.erp.domain.entities.TrainingDate;
import com.microservice.erp.domain.entities.Username;
import com.microservice.erp.services.iServices.ITrainingDateService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/trainingDate")
@AllArgsConstructor
public class TrainingDateController {

    private final ITrainingDateService iTrainingDateService;


    @PostMapping("/saveTrainingDate")
    public ResponseEntity<?> saveTrainingDate(@RequestHeader(HttpHeaders.AUTHORIZATION) String token
            , @RequestBody TrainingDate trainingDate) {

        JWTPayload jwtPayload = TokenValidator.parsePayload(token, JWTPayload.class);
        BigInteger userId = new BigInteger(jwtPayload.getSub());

        return iTrainingDateService.saveTrainingDate(userId, trainingDate);
    }

    @PostMapping("/updateTrainingDate")
    public ResponseEntity<?> updateTrainingDate(@RequestHeader(HttpHeaders.AUTHORIZATION) String token
            , @RequestBody TrainingDate trainingDate) {

        JWTPayload jwtPayload = TokenValidator.parsePayload(token, JWTPayload.class);
        BigInteger userId = new BigInteger(jwtPayload.getSub());

        return iTrainingDateService.updateTrainingDate(userId, trainingDate);
    }

    @GetMapping("/getTrainingDate")
    public ResponseEntity<?> getTrainingDate() {
        return iTrainingDateService.getTrainingDate();
    }

    @GetMapping("/getActiveTrainingDate")
    public ResponseEntity<?> getActiveTrainingDate() {
        return iTrainingDateService.getActiveTrainingDate();
    }

}
