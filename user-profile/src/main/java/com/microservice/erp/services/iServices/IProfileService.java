package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.UserProfileDto;
import com.microservice.erp.domain.helper.ResponseMessage;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;

public interface IProfileService {

    ResponseEntity<?> getProfileInfo(String authHeader, Long userId);

    ResponseEntity<?> getProfilePicture(Long userId) throws IOException;

    ResponseEntity<?> changeUsername(UserProfileDto userProfileDto);

    ResponseEntity<?> changeMobileNo(UserProfileDto userProfileDto);

    ResponseEntity<?> checkEmailExistOrNot(String email);

    ResponseEntity<?> changeEmail(UserProfileDto userProfileDto);

    ResponseEntity<?> receiveOtp(UserProfileDto userProfileDto);

    ResponseEntity<?> receiveEmailVcode(UserProfileDto userProfileDto);

    ResponseEntity<?> changePassword(UserProfileDto userProfileDto);

    ResponseEntity<?> changeParentInfo(UserProfileDto userProfileDto);

    ResponseEntity<?> changeGuardianInfo(UserProfileDto userProfileDto);

    ResponseEntity<?> changeSocialMediaLink(UserProfileDto userProfileDto);

    ResponseEntity<?> getAllDzongkhags(String authHeader);

    ResponseEntity<?> getGeogByDzongkhagId(String authHeader, Integer dzongkhagId);

    ResponseEntity<?> changeCurrentAddress(UserProfileDto userProfileDto);

    ResponseEntity<?> syncCensusRecord(UserProfileDto userProfileDto) throws ParseException;

    ResponseEntity<?> searchUser(String searchKey);

    ResponseEntity<?> resetUserPassword(UserProfileDto userProfileDto);

    ResponseEntity<?> changeProfilePic(HttpServletRequest request, UserProfileDto userProfileDto) throws IOException;

    ResponseEntity<?> getRegisteredUsers();
}
