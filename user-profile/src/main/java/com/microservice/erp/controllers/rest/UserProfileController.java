package com.microservice.erp.controllers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jcraft.jsch.*;
import com.microservice.erp.domain.dto.UserProfileDto;
import com.microservice.erp.services.iServices.IProfileService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
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
import java.util.Vector;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/userProfile")
public class UserProfileController {
    private final IProfileService iProfileService;

    public UserProfileController(IProfileService iProfileService) {
        this.iProfileService = iProfileService;
    }

    @RequestMapping(value = "/downloadFileLocal", method = RequestMethod.GET)
    public ResponseEntity<?> downloadFileLocal(@RequestParam("url") String url) {
        url = "/opt/gyalsungDocuments/userProfile/gyalsung/2023/testFile.png";
        FileSystemResource file = new FileSystemResource(new File(url));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public ResponseEntity<?> downloadFile(@RequestParam("url") String url) {
        String server = "172.30.84.147";
        int port = 22;
        String username = "sysadmin";
        String password = "Sys@2023";
        String remoteFilePath = "/home/sysadmin/opt/gyalsungDocument/edeDocument/2023/Aug/11/alert_message.pdf";
        String localDirectory = "/path/to/local/directory";

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(username, server, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no"); // Use with caution
            session.connect();

            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            // List details about the remote file
            Vector<ChannelSftp.LsEntry> entries = channelSftp.ls(remoteFilePath);
            if (entries.size() > 0) {
                ChannelSftp.LsEntry entry = entries.get(0);
//                System.out.println("File Name: " + entry.getFilename());
//                System.out.println("File Size: " + entry.getAttrs().getSize());
//                System.out.println("File Permissions: " + entry.getAttrs().getPermissionsString());
                // Add more details as needed

//                Resource resource = new FileSystemResource(remoteFilePath + entry.getFilename());
                Resource resource = new FileSystemResource(remoteFilePath);
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
            } else {
                System.out.println("Remote file not found.");
            }
            channelSftp.disconnect();
            session.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("File fetch failed.");
        }

        return null;
    }

    @RequestMapping(value = "/getProfilePicture", method = RequestMethod.GET)
    public ResponseEntity<?> getProfilePicture(@RequestParam("userId") BigInteger userId) throws IOException {
        return iProfileService.getProfilePicture(userId);
    }

    @RequestMapping(value = "/getProfilePictureByCid", method = RequestMethod.GET)
    public ResponseEntity<?> getProfilePictureByCid(@RequestParam("cid") String cid) throws IOException {
        return iProfileService.getProfilePictureByCid(cid);
    }

    @GetMapping("/getProfileInfo")
    public ResponseEntity<?> getProfileInfo(@RequestHeader("Authorization") String authHeader, @RequestParam("userId") BigInteger userId) {
        return iProfileService.getProfileInfo(authHeader, userId);
    }

    @GetMapping("/getProfileInfoByCid")
    public ResponseEntity<?> getProfileInfoByCid(@RequestHeader("Authorization") String authHeader, @RequestParam("cid") String cid) {
        return iProfileService.getProfileInfoByCid(authHeader, cid);
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
    public ResponseEntity<?> changeMobileNo(@RequestBody UserProfileDto userProfileDto) throws JsonProcessingException {
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
    public ResponseEntity<?> changeProfilePic(HttpServletRequest request, @ModelAttribute UserProfileDto userProfileDto) throws IOException, JSchException, SftpException {
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
