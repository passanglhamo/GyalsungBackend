package com.microservice.erp.services.iServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dto.UserProfileDto;
import com.microservice.erp.domain.helper.ResponseMessage;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface IProfileService {

    ResponseEntity<?> getProfileInfo(String authHeader, BigInteger userId);

    ResponseEntity<?> getProfileInfoByCid(String authHeader, String cid);

    ResponseEntity<?> getProfilePicture(BigInteger userId) throws IOException;

    ResponseEntity<?> getProfilePictureByCid(String cid) throws IOException;

    ResponseEntity<?> changeMobileNo(UserProfileDto userProfileDto);

    ResponseEntity<?> checkEmailExistOrNot(String email);

    ResponseEntity<?> changeEmail(UserProfileDto userProfileDto) throws JsonProcessingException;

    ResponseEntity<?> changeUsername(UserProfileDto userProfileDto) throws JsonProcessingException;

    ResponseEntity<?> receiveOtp(UserProfileDto userProfileDto) throws JsonProcessingException;

    ResponseEntity<?> receiveEmailVcode(UserProfileDto userProfileDto) throws Exception;


    ResponseEntity<?> changeParentInfo(UserProfileDto userProfileDto);

    ResponseEntity<?> changeGuardianInfo(UserProfileDto userProfileDto);

    ResponseEntity<?> changeSocialMediaLink(UserProfileDto userProfileDto);

    ResponseEntity<?> changeCurrentAddress(UserProfileDto userProfileDto);

    ResponseEntity<?> syncCensusRecord(UserProfileDto userProfileDto) throws ParseException;

    ResponseEntity<?> searchUser(String searchKey);

    ResponseEntity<?> changeProfilePic(HttpServletRequest request, UserProfileDto userProfileDto) throws IOException;

    ResponseEntity<?> getRegisteredUsers();

    ResponseEntity<?> getAllUsersEligibleForTraining(Date paramDate, Integer paramAge);

    ResponseEntity<?> checkUnderAge(BigInteger userId, Date paramDate);

    ResponseEntity<?> getProfileInfoByIds(List<BigInteger> userIds);

    ResponseEntity<?> getUserInformationByPartialCid(String cid);
}
