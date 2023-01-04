
package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.ResetPasswordDto;
import com.microservice.erp.domain.dto.UserProfileDto;
import com.microservice.erp.services.definition.ISecuritySettingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/securitySetting")
@AllArgsConstructor
public class SecuritySettingController {

    private final ISecuritySettingService iSecuritySettingService;

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody UserProfileDto userProfileDto) {
        return iSecuritySettingService.changePassword(userProfileDto);
    }

    @PostMapping("/changeUsername")
    public ResponseEntity<?> changeUsername(@RequestBody UserProfileDto userProfileDto) {
        return iSecuritySettingService.changeUsername(userProfileDto);
    }

    @PostMapping("/resetUserPassword")
    public ResponseEntity<?> resetUserPassword(@RequestBody UserProfileDto userProfileDto) {
        return iSecuritySettingService.resetUserPassword(userProfileDto);
    }

    //region forgot password
    @RequestMapping(value = "/requestPasswordChange", method = RequestMethod.POST)
    public ResponseEntity<?> requestPasswordChange(@RequestBody ResetPasswordDto resetPasswordDto) throws Exception {
        return iSecuritySettingService.requestPasswordChange(resetPasswordDto);
    }

    @RequestMapping(value = "/validatePasswordResetLink", method = RequestMethod.POST)
    public ResponseEntity<?> validatePasswordResetLink(@RequestBody ResetPasswordDto resetPasswordDto) {
        return iSecuritySettingService.validatePasswordResetLink(resetPasswordDto);
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        return iSecuritySettingService.resetPassword(resetPasswordDto);
    }
    //endregion

}

