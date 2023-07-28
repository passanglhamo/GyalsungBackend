
package com.microservice.erp.controllers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dto.ResetPasswordDto;
import com.microservice.erp.domain.dto.UserProfileDto;
import com.microservice.erp.services.definition.ISecuritySettingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/securitySetting")
@AllArgsConstructor
public class SecuritySettingController {

    private final ISecuritySettingService iSecuritySettingService;

    @PostMapping("/getUserInfoByCidAndDob")
    public ResponseEntity<?> getUserInfoByCidAndDob(@RequestBody UserProfileDto userProfileDto) throws ParseException {
        return iSecuritySettingService.getUserInfoByCidAndDob(userProfileDto);
    }

    @PostMapping("/receiveResetPasswordOtp")
    public ResponseEntity<?> receiveResetPasswordOtp(@RequestBody UserProfileDto userProfileDto) throws ParseException, JsonProcessingException {
        return iSecuritySettingService.receiveResetPasswordOtp(userProfileDto);
    }

    @PostMapping("/verifyResetPwOtp")
    public ResponseEntity<?> verifyResetPwOtp(@RequestBody UserProfileDto userProfileDto) {
        return iSecuritySettingService.verifyResetPwOtp(userProfileDto);
    }

    @PostMapping("/resetPasswordUsingMobileNo")
    public ResponseEntity<?> resetPasswordUsingMobileNo(@RequestBody UserProfileDto userProfileDto) {
        return iSecuritySettingService.resetPasswordUsingMobileNo(userProfileDto);
    }

    @PostMapping("/getPasswordResetLink")
    public ResponseEntity<?> getPasswordResetLink(@RequestBody ResetPasswordDto resetPasswordDto) throws Exception {
        return iSecuritySettingService.getPasswordResetLink(resetPasswordDto);
    }

    @RequestMapping(value = "/validatePasswordResetLink", method = RequestMethod.POST)
    public ResponseEntity<?> validatePasswordResetLink(@RequestBody ResetPasswordDto resetPasswordDto) {
        return iSecuritySettingService.validatePasswordResetLink(resetPasswordDto);
    }

    @RequestMapping(value = "/resetPasswordUsingEmail", method = RequestMethod.POST)
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        return iSecuritySettingService.resetPasswordUsingEmail(resetPasswordDto);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody UserProfileDto userProfileDto) {
        return iSecuritySettingService.changePassword(userProfileDto);
    }

    @PostMapping("/resetUserPassword")
    public ResponseEntity<?> resetUserPassword(@RequestBody UserProfileDto userProfileDto) {
        return iSecuritySettingService.resetUserPassword(userProfileDto);
    }

    //endregion

}

