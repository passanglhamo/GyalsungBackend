package com.microservice.erp.controllers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dto.UserProfileDto;
import com.microservice.erp.services.iServices.IProfileService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

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
    public ResponseEntity<?> getProfilePicture(@RequestParam("userId") BigInteger userId) throws IOException {
        return iProfileService.getProfilePicture(userId);
    }

    @GetMapping("/getProfileInfo")
    public ResponseEntity<?> getProfileInfo(@RequestHeader("Authorization") String authHeader, @RequestParam("userId") BigInteger userId) {
        return iProfileService.getProfileInfo(authHeader, userId);
    }

    @GetMapping("/getProfileInfoByIds")
    public ResponseEntity<?> getProfileInfoByIds(@RequestParam("userIds") List<BigInteger> userIds) {
        return iProfileService.getProfileInfoByIds(userIds);
    }

    @GetMapping("/getUserInformationByPartialCid")
    public ResponseEntity<?> getUserInformationByPartialCid(@RequestParam("cid") String cid) {
        return iProfileService.getUserInformationByPartialCid(cid);
    }

    @PostMapping("/receiveOtp")
    public ResponseEntity<?> receiveOtp(@RequestBody UserProfileDto userProfileDto) throws JsonProcessingException {
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
    public ResponseEntity<?> receiveEmailVcode(@RequestBody UserProfileDto userProfileDto) throws Exception {
        return iProfileService.receiveEmailVcode(userProfileDto);
    }

    @PostMapping("/changeEmail")
    public ResponseEntity<?> changeEmail(@RequestBody UserProfileDto userProfileDto) throws JsonProcessingException {
        return iProfileService.changeEmail(userProfileDto);
    }

    @PostMapping("/changeUsername")
    public ResponseEntity<?> changeUsername(@RequestBody UserProfileDto userProfileDto) throws JsonProcessingException {
        return iProfileService.changeUsername(userProfileDto);
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

    @PostMapping("/syncCensusRecord")
    public ResponseEntity<?> syncCensusRecord(@RequestBody UserProfileDto userProfileDto) throws ParseException {
        return iProfileService.syncCensusRecord(userProfileDto);
    }

    @GetMapping("/searchUser")
    public ResponseEntity<?> searchUser(@RequestParam("searchKey") String searchKey) {
        return iProfileService.searchUser(searchKey);
    }

    @PostMapping("/changeProfilePic")
    public ResponseEntity<?> changeProfilePic(HttpServletRequest request, @ModelAttribute UserProfileDto userProfileDto) throws IOException {
        return iProfileService.changeProfilePic(request, userProfileDto);
    }

    @GetMapping("/getRegisteredUsers")
    public ResponseEntity<?> getRegisteredUsers() {
        return iProfileService.getRegisteredUsers();
    }

    /**
     * this api is used for sending notifications in notification service, Initial Notification,
     * Reminder Notification and Final Notifications
     *
     * @return -- ResponseEntity<?>
     */
    @GetMapping("/getAllUsersEligibleForTraining")
    public ResponseEntity<?> getAllUsersEligibleForTraining(@RequestParam("paramDate") Date paramDate
            , @RequestParam("paramAge") Integer paramAge) {
        return iProfileService.getAllUsersEligibleForTraining(paramDate, paramAge);
    }

    /**
     * to get age during enrolment time to check if the user is underage or not
     *
     * @param userId    -- BigInteger
     * @param paramDate -- Date
     * @return -- ResponseEntity<?>
     */
    @GetMapping("/checkUnderAge")
    public ResponseEntity<?> checkUnderAge(@RequestParam("userId") BigInteger userId,
                                           @RequestParam("paramDate") Date paramDate) {
        return iProfileService.checkUnderAge(userId, paramDate);
    }
}
