package com.microservice.erp.controllers.rest;

import com.infoworks.lab.jjwt.JWTPayload;
import com.infoworks.lab.jjwt.TokenValidator;
import com.microservice.erp.domain.entities.Reason;
import com.microservice.erp.services.iServices.ICreateReasonService;
import com.microservice.erp.services.iServices.IReadReasonService;
import com.microservice.erp.services.iServices.IUpdateReasonService;
import com.microservice.erp.services.impl.services.SpringSecurityAuditorAware;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/reasons")
@AllArgsConstructor
public class ReasonController {

    private final IReadReasonService readService;
    private final ICreateReasonService service;
    private final IUpdateReasonService updateService;

    @PostMapping
    public Reason saveReason(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                             @Valid @RequestBody Reason reason) {
        SpringSecurityAuditorAware.setToken(token);
        return service.saveReason(reason);
    }


    @GetMapping("/getAllReasonById")
    public Reason getAllReasonById(@RequestParam("id") BigInteger id) {
        return readService.getAllReasonById(id);
    }

    @GetMapping
    public List<Reason> getAllReasonList(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

        //region sample code to get userId from JWT token
        JWTPayload jwtPayload = TokenValidator.parsePayload(token, JWTPayload.class);
        String userId = jwtPayload.getSub();
        //endregion

        return readService.getAllReasonList();
    }

    @GetMapping("/getAllReasonByStatus")
    public List<Reason> getAllReasonByStatus(@RequestParam("status") String status) {
        return readService.getAllReasonByStatus(status);
    }

    @PutMapping("/updateReason")
    public ResponseEntity<?> updateReason(@RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                          @Valid @RequestBody Reason reason) {
        SpringSecurityAuditorAware.setToken(token);
        return updateService.updateReason(reason);
    }

}
