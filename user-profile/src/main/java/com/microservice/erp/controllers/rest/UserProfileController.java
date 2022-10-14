package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.UserProfileDto;
import com.microservice.erp.domain.helper.ResponseMessage;
import com.microservice.erp.services.iServices.IProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/userProfile")
public class UserProfileController {
    private final IProfileService iProfileService;

    public UserProfileController(IProfileService iProfileService) {
        this.iProfileService = iProfileService;
    }

    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public ResponseEntity<?> downloadFile(@RequestParam("url") String url) {
        FileSystemResource file = new FileSystemResource(new File(url));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @RequestMapping(value = "/getProfilePicture", method = RequestMethod.GET)
    public ResponseEntity<?> getProfilePicture(@RequestParam("userId") Long userId) throws IOException {
        return iProfileService.getProfilePicture(userId);
    }

    @GetMapping("/getProfileInfo")
    public ResponseEntity<?> getProfileInfo(@RequestParam("userId") Long userId) {
        return iProfileService.getProfileInfo(userId);
    }

    @PostMapping("/changeUsername")
    public ResponseEntity<?> changeUsername(@RequestBody UserProfileDto userProfileDto) {
        return iProfileService.changeUsername(userProfileDto);
    }

    @PostMapping("/receiveOtp")
    public ResponseEntity<?> receiveOtp(@RequestBody UserProfileDto userProfileDto) {
        return iProfileService.receiveOtp(userProfileDto);
    }

    @PostMapping("/changeMobileNo")
    public ResponseEntity<?> changeMobileNo(@RequestBody UserProfileDto userProfileDto) {
        return iProfileService.changeMobileNo(userProfileDto);
    }

    @GetMapping("/checkEmailExistOrNot")
    public ResponseEntity<?> checkEmailExistOrNot(@RequestParam("email") String email) {
        return iProfileService.checkEmailExistOrNot(email);
    }

    @PostMapping("/receiveEmailVcode")
    public ResponseEntity<?> receiveEmailVcode(@RequestBody UserProfileDto userProfileDto) {
        return iProfileService.receiveEmailVcode(userProfileDto);
    }

    @PostMapping("/changeEmail")
    public ResponseEntity<?> changeEmail(@RequestBody UserProfileDto userProfileDto) {
        return iProfileService.changeEmail(userProfileDto);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody UserProfileDto userProfileDto) {
        return iProfileService.changePassword(userProfileDto);
    }

    @PostMapping("/changeParentInfo")
    public ResponseEntity<?> changeParentInfo(@RequestBody UserProfileDto userProfileDto) {
        return iProfileService.changeParentInfo(userProfileDto);
    }

    @PostMapping("/changeGuardianInfo")
    public ResponseEntity<?> changeGuardianInfo(@RequestBody UserProfileDto userProfileDto) {
        return iProfileService.changeGuardianInfo(userProfileDto);
    }

    @PostMapping("/changeSocialMediaLink")
    public ResponseEntity<?> changeSocialMediaLink(@RequestBody UserProfileDto userProfileDto) {
        return iProfileService.changeSocialMediaLink(userProfileDto);
    }

    @PostMapping("/changeCurrentAddress")
    public ResponseEntity<?> changeCurrentAddress(@RequestBody UserProfileDto userProfileDto) {
        return iProfileService.changeCurrentAddress(userProfileDto);
    }

    @GetMapping("/getAllDzongkhags")
    public ResponseEntity<?> allDzongkhags() {
        return iProfileService.getAllDzongkhags();
    }

    @GetMapping("/getGeogByDzongkhagId")
    public ResponseEntity<?> getGeogByDzongkhagId(@RequestParam("dzongkhagId") Integer dzongkhagId) {
        return iProfileService.getGeogByDzongkhagId(dzongkhagId);
    }

    @PostMapping("/syncCensusRecord")
    public ResponseEntity<?> syncCensusRecord(@RequestBody UserProfileDto userProfileDto) throws ParseException {
        return iProfileService.syncCensusRecord(userProfileDto);
    }

    @GetMapping("/searchUser")
    public ResponseEntity<?> searchUser(@RequestParam("searchKey") String searchKey) {
        return iProfileService.searchUser(searchKey);
    }

    @PostMapping("/resetUserPassword")
    public ResponseEntity<?> resetUserPassword(@RequestBody UserProfileDto userProfileDto) {
        return iProfileService.resetUserPassword(userProfileDto);
    }

    @PostMapping("/changeProfilePic")
    public ResponseEntity<?> changeProfilePic(HttpServletRequest request, @ModelAttribute UserProfileDto userProfileDto) throws IOException {
        return iProfileService.changeProfilePic(request, userProfileDto);
    }

}
