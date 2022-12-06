package com.microservice.erp.controllers.rest;


import com.microservice.erp.domain.dto.ResetPasswordDto;
import com.microservice.erp.services.iServices.IResetPasswordService;
import com.microservice.erp.services.impl.ResetPasswordService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*")
@RestController
@AllArgsConstructor
@RequestMapping("/resetPassword")
public class ResetPasswordController {
    private final IResetPasswordService iResetPasswordService;

    @RequestMapping(value = "/requestPasswordChange", method = RequestMethod.POST)
    public ResponseEntity<?> requestPasswordChange(@RequestBody ResetPasswordDto resetPasswordDto) throws Exception {
        return iResetPasswordService.requestPasswordChange(resetPasswordDto);
    }

    @RequestMapping(value = "/validatePasswordResetLink", method = RequestMethod.POST)
    public ResponseEntity<?> validatePasswordResetLink(@RequestBody ResetPasswordDto resetPasswordDto) {
        return iResetPasswordService.validatePasswordResetLink(resetPasswordDto);
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        return iResetPasswordService.resetPassword(resetPasswordDto);
    }
}
