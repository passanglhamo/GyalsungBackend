package com.microservice.erp.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dao.UserDao;
import com.microservice.erp.domain.dto.*;
import com.microservice.erp.domain.entities.ChangeEmailVerificationCode;
import com.microservice.erp.domain.entities.ChangeMobileNoSmsOtp;
import com.microservice.erp.domain.entities.UserInfo;
import com.microservice.erp.domain.helper.FileUploadDTO;
import com.microservice.erp.domain.helper.FileUploadToExternalLocation;
import com.microservice.erp.domain.helper.OTPGenerator;
import com.microservice.erp.domain.helper.ResponseMessage;
import com.microservice.erp.domain.repositories.IChangeEmailVerificationCodeRepository;
import com.microservice.erp.domain.repositories.IChangeMobileNoSmsOtpRepository;
import com.microservice.erp.domain.repositories.IUserInfoRepository;
import com.microservice.erp.services.iServices.IProfileService;
import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ProfileService implements IProfileService {
    private final UserDao userDao;
    private final IUserInfoRepository iUserInfoRepository;
    private final IChangeMobileNoSmsOtpRepository iChangeMobileNoSmsOtpRepository;
    private final IChangeEmailVerificationCodeRepository iChangeEmailVerificationCodeRepository;

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvxyz0123456789";
    private final AddToQueue addToQueue;

    //@Autowired
    //@Qualifier("trainingManagementTemplate")
    private RestTemplate restTemplate;

    public ProfileService(UserDao userDao, IUserInfoRepository iUserInfoRepository
            , IChangeMobileNoSmsOtpRepository iChangeMobileNoSmsOtpRepository
            , IChangeEmailVerificationCodeRepository iChangeEmailVerificationCodeRepository
            , AddToQueue addToQueue
            , @Qualifier("trainingManagementTemplate") RestTemplate restTemplate) {
        this.userDao = userDao;
        this.iUserInfoRepository = iUserInfoRepository;
        this.iChangeMobileNoSmsOtpRepository = iChangeMobileNoSmsOtpRepository;
        this.iChangeEmailVerificationCodeRepository = iChangeEmailVerificationCodeRepository;
        this.addToQueue = addToQueue;
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<?> getProfileInfo(String authHeader, BigInteger userId) {
        UserInfo userInfo = iUserInfoRepository.findById(userId).get();
        return profileInfo(authHeader, userInfo);
    }

    private ResponseEntity<?> profileInfo(String authHeader, UserInfo userInfo) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);

        UserProfileDto userProfileDto = new ModelMapper().map(userInfo, UserProfileDto.class);
        userProfileDto.setPassword(null);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<String> request = new HttpEntity<>(headers);
        if (userInfo.getPresentGeogId() != null) {
            String geogUrl = properties.getTrainingManGeogByGeogId() + userInfo.getPresentGeogId();
            ResponseEntity<GeogDto> geogResponse = restTemplate.exchange(geogUrl, HttpMethod.GET, request, GeogDto.class);
            userProfileDto.setPresentGeogName(Objects.requireNonNull(geogResponse.getBody()).getGeogName());
        }
        if (userInfo.getPresentDzongkhagId() != null) {
            String dzongkhagUrl = properties.getTrainingManDzongkhagByDzongkhagId() + userInfo.getPresentDzongkhagId();
            ResponseEntity<DzongkhagDto> dzongkhagResponse = restTemplate.exchange(dzongkhagUrl, HttpMethod.GET, request, DzongkhagDto.class);
            userProfileDto.setPresentDzongkhagName(Objects.requireNonNull(dzongkhagResponse.getBody()).getDzongkhagName());
        }
        return ResponseEntity.ok(userProfileDto);
    }


    @Override
    public ResponseEntity<?> getProfileInfoByCid(String authHeader, String cid) {
        UserInfo userInfo = iUserInfoRepository.findByCid(cid).get();
        return profileInfo(authHeader, userInfo);
    }

    @Override
    public ResponseEntity<?> getProfilePicture(BigInteger userId) throws IOException {
        UserInfo userInfo = iUserInfoRepository.findById(userId).get();
        String profilePictureUrl = userInfo.getProfilePictureUrl();
        if (profilePictureUrl == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Profile picture not set yet."));
        } else {
            UserProfileDto userProfileDto = new UserProfileDto();
            Path url = Paths.get(profilePictureUrl);// retrieve the image by its url
            userProfileDto.setProfilePhoto(IOUtils.toByteArray(url.toUri()));
            return ResponseEntity.ok(userProfileDto);
        }
    }

    @Override
    public ResponseEntity<?> getProfilePictureByCid(String cid) throws IOException {
        UserInfo userInfo = iUserInfoRepository.findByCid(cid).get();
        String profilePictureUrl = userInfo.getProfilePictureUrl();
        if (profilePictureUrl == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Profile picture not set yet."));
        } else {
            UserProfileDto userProfileDto = new UserProfileDto();
            Path url = Paths.get(profilePictureUrl);// retrieve the image by its url
            userProfileDto.setProfilePhoto(IOUtils.toByteArray(url.toUri()));
            return ResponseEntity.ok(userProfileDto);
        }
    }

    public ResponseEntity<?> receiveOtp(UserProfileDto userProfileDto) throws JsonProcessingException {
//        Optional<UserInfo> userInfoDB = iUserInfoRepository.findByMobileNo(userProfileDto.getMobileNo());
//        if (userInfoDB.isPresent()) {
//            return ResponseEntity.badRequest().body(new MessageResponse("The mobile number you have entered is already in use. Please try a different one."));
//        }

        String otp = OTPGenerator.generateOtp();

        String message = "Your OTP to change mobile number in Gyalsung System is " + otp + " Please use this within 3 minutes.";
        EventBus eventBusSms = EventBus.withId(null, null, null, message, null, userProfileDto.getMobileNo());

        ChangeMobileNoSmsOtp changeMobileNoSmsOtp = new ChangeMobileNoSmsOtp();
        changeMobileNoSmsOtp.setUserId(userProfileDto.getUserId());
        changeMobileNoSmsOtp.setMobileNo(userProfileDto.getMobileNo());
        changeMobileNoSmsOtp.setOtp(otp);
        changeMobileNoSmsOtp.setDate(new Date());
        changeMobileNoSmsOtp.setExpiryTime(180);//180 seconds
        iChangeMobileNoSmsOtpRepository.save(changeMobileNoSmsOtp);
        addToQueue.addToQueue("sms", eventBusSms);
        return ResponseEntity.ok(changeMobileNoSmsOtp);
    }


    @Override
    public ResponseEntity<?> checkEmailExistOrNot(String email) {
        Optional<UserInfo> userInfoDb = iUserInfoRepository.findByEmail(email);
        if (userInfoDb.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email already in use."));
        } else {
            return ResponseEntity.ok(new MessageResponse("Email available."));
        }
    }

    @Override
    public ResponseEntity<?> changeMobileNo(UserProfileDto userProfileDto) throws JsonProcessingException {
        UserInfo userInfoDb = iUserInfoRepository.findById(userProfileDto.getUserId()).get();

//        Optional<UserInfo> userInfoDB = iUserInfoRepository.findByMobileNo(userProfileDto.getMobileNo());
//        if (userInfoDB.isPresent()) {
//            return ResponseEntity.badRequest().body(new MessageResponse("The mobile number you have entered is already in use. Please try a different one."));
//        }

        //verify otp
        ResponseEntity<?> responseEntity = verifyOtp(userProfileDto);
        if (responseEntity.getStatusCode().value() != HttpStatus.OK.value()) {
            return ResponseEntity.badRequest().body(new MessageResponse("OTP didn't match."));
        }
        UserInfo userInfoObject = new ModelMapper().map(userInfoDb, UserInfo.class);

        userInfoObject.setMobileNo(userProfileDto.getMobileNo());
        iUserInfoRepository.save(userInfoDb);
        iChangeMobileNoSmsOtpRepository.deleteById(userInfoDb.getUserId());

        EventBusUser eventBusUser = EventBusUser.withId(userInfoDb.getUserId(), null, null, null, null
                , userInfoDb.getMobileNo(), null, null, null, null);

        addToQueue.addToUserQueue("changeMobileNo", eventBusUser);
        return ResponseEntity.ok(new MessageResponse("Mobile number changed successfully."));
    }

    @Override
    public ResponseEntity<?> changeEmail(UserProfileDto userProfileDto) throws JsonProcessingException {

        UserInfo userInfoDb = iUserInfoRepository.findById(userProfileDto.getUserId()).get();
        UserInfo userInfo = new ModelMapper().map(userInfoDb, UserInfo.class);

        //verify verification code
        ResponseEntity<?> responseEntity = verifyVerificationCode(userProfileDto);
        if (responseEntity.getStatusCode().value() != HttpStatus.OK.value()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Verification code didn't match."));
        }
//        ResponseEntity<?> checkEmailExistOrNot = checkEmailExistOrNot(userProfileDto.getEmail());
//        if (checkEmailExistOrNot.getStatusCode().value() != HttpStatus.OK.value()) {
//            return ResponseEntity.badRequest().body(new MessageResponse("Email already in use."));
//        }
        userInfoDb.setEmail(userProfileDto.getEmail());
        iUserInfoRepository.save(userInfoDb);

        iChangeEmailVerificationCodeRepository.deleteById(userInfoDb.getUserId());

        //add to queue to update email in auth microservices
        EventBusUser eventBusUser = EventBusUser.withId(userInfoDb.getUserId(), null, null, null, userInfo.getEmail()
                , null, null, null, null, null);

        addToQueue.addToUserQueue("changeEmail", eventBusUser);
        return ResponseEntity.ok(new MessageResponse("Email changed successfully."));
    }

    @Override
    public ResponseEntity<?> changeUsername(UserProfileDto userProfileDto) throws JsonProcessingException {
        UserInfo userInfoDb = iUserInfoRepository.findById(userProfileDto.getUserId()).get();
        ResponseEntity<?> checkUsernameExistOrNot = checkUsernameExistOrNot(userProfileDto.getUsername());
        if (checkUsernameExistOrNot.getStatusCode().value() != HttpStatus.OK.value()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username already in use."));
        }
        UserInfo userInfo = new ModelMapper().map(userInfoDb, UserInfo.class);
        userInfo.setUsername(userProfileDto.getUsername());
        iUserInfoRepository.save(userInfo);
        //add to queue to update username in auth microservices
        EventBusUser eventBusUser = EventBusUser.withId(userInfoDb.getUserId(), null, null, null, null, null
                , userInfo.getUsername(), null, null, null);
        addToQueue.addToUserQueue("changeUsername", eventBusUser);
        return ResponseEntity.ok(new MessageResponse("Username updated successfully."));
    }


    private ResponseEntity<?> checkUsernameExistOrNot(String username) {
        Optional<UserInfo> userInfoDb = iUserInfoRepository.findByUsername(username);
        if (userInfoDb.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username already in use."));
        } else {
            return ResponseEntity.ok(new MessageResponse("Username available."));
        }
    }

    @Override
    public ResponseEntity<?> receiveEmailVcode(UserProfileDto userProfileDto) throws Exception {
//        Optional<UserInfo> userInfoDb = iUserInfoRepository.findByEmail(userProfileDto.getEmail());
//        if (userInfoDb.isPresent()) {
//            return ResponseEntity.badRequest().body(new MessageResponse("Email already in use."));
//        }
        String verificationCode = OTPGenerator.generateOtp();
        String subject = "Email Verification";
        String message = "Dear User,<br></br>" +
                " The verification code to change email in Gyalsung System is " + verificationCode + ".<br></br><br></br>" +
                "<small>***This is a system-generated email. Please do not respond to this email.***</small>";

        EventBus eventBusEmail = EventBus.withId(userProfileDto.getEmail(), null, null, message, subject, null);

        ChangeEmailVerificationCode changeEmailVerificationCode = new ChangeEmailVerificationCode();
        changeEmailVerificationCode.setUserId(userProfileDto.getUserId());
        changeEmailVerificationCode.setEmail(userProfileDto.getEmail());
        changeEmailVerificationCode.setVerificationCode(verificationCode);
        changeEmailVerificationCode.setDate(new Date());
        changeEmailVerificationCode.setExpiryTime(180);//1880 seconds
        iChangeEmailVerificationCodeRepository.save(changeEmailVerificationCode);
        addToQueue.addToQueue("email", eventBusEmail);
        return ResponseEntity.ok(changeEmailVerificationCode);
    }

    @Override
    public ResponseEntity<?> changeParentInfo(UserProfileDto userProfileDto) {
        UserInfo userInfo = iUserInfoRepository.findById(userProfileDto.getUserId()).get();

        userInfo.setFatherCid(userProfileDto.getFatherCid());
        userInfo.setFatherMobileNo(userProfileDto.getFatherMobileNo());
        userInfo.setFatherEmail(userProfileDto.getFatherEmail());
        userInfo.setFatherOccupation(userProfileDto.getFatherOccupation());

        userInfo.setMotherCid(userProfileDto.getFatherCid());
        userInfo.setMotherMobileNo(userProfileDto.getMotherMobileNo());
        userInfo.setMotherEmail(userProfileDto.getMotherEmail());
        userInfo.setMotherOccupation(userProfileDto.getMotherOccupation());

        iUserInfoRepository.save(userInfo);
        return ResponseEntity.ok(new MessageResponse("Parent information updated successfully."));
    }

    @Override
    public ResponseEntity<?> changeGuardianInfo(UserProfileDto userProfileDto) {
        UserInfo userInfo = iUserInfoRepository.findById(userProfileDto.getUserId()).get();

        userInfo.setGuardianNameFirst(userProfileDto.getGuardianNameFirst());
        userInfo.setGuardianCidFirst(userProfileDto.getGuardianCidFirst());
        userInfo.setGuardianOccupationFirst(userProfileDto.getGuardianOccupationFirst());
        userInfo.setGuardianMobileNoFirst(userProfileDto.getGuardianMobileNoFirst());
        userInfo.setGuardianEmailFirst(userProfileDto.getGuardianEmailFirst());
        userInfo.setRelationToGuardianFirst(userProfileDto.getRelationToGuardianFirst());

        userInfo.setGuardianNameSecond(userProfileDto.getGuardianNameSecond());
        userInfo.setGuardianCidSecond(userProfileDto.getGuardianCidSecond());
        userInfo.setGuardianOccupationSecond(userProfileDto.getGuardianOccupationSecond());
        userInfo.setGuardianMobileNoSecond(userProfileDto.getGuardianMobileNoSecond());
        userInfo.setGuardianEmailSecond(userProfileDto.getGuardianEmailSecond());
        userInfo.setRelationToGuardianSecond(userProfileDto.getRelationToGuardianSecond());

        iUserInfoRepository.save(userInfo);
        return ResponseEntity.ok(new MessageResponse("Guardian information updated successfully."));
    }

    @Override
    public ResponseEntity<?> changeSocialMediaLink(UserProfileDto userProfileDto) {
        UserInfo userInfo = iUserInfoRepository.findById(userProfileDto.getUserId()).get();
        userInfo.setSocialMediaLink1(userProfileDto.getSocialMediaLink1());
        userInfo.setSocialMediaLink2(userProfileDto.getSocialMediaLink2());
        userInfo.setSocialMediaLink3(userProfileDto.getSocialMediaLink3());
        iUserInfoRepository.save(userInfo);
        return ResponseEntity.ok(new MessageResponse("Social media link updated successfully."));

    }

    private ResponseEntity<?> verifyVerificationCode(UserProfileDto userProfileDto) {
        ChangeEmailVerificationCode changeEmailVerificationCode = iChangeEmailVerificationCodeRepository.findById(userProfileDto.getUserId()).get();
        if (changeEmailVerificationCode == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Verification code not found matching email " + userProfileDto.getEmail()));
        }
        if (Objects.equals(changeEmailVerificationCode.getVerificationCode(), userProfileDto.getVerificationCode())) {
            return ResponseEntity.ok("");
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Verification code didn't match."));
        }
    }


    @Override
    public ResponseEntity<?> changeCurrentAddress(UserProfileDto userProfileDto) {
        UserInfo userInfo = iUserInfoRepository.findById(userProfileDto.getUserId()).get();
        userInfo.setPresentCountry(userProfileDto.getPresentCountry());
        userInfo.setPresentPlaceName(userProfileDto.getPresentPlaceName());
        userInfo.setPresentDzongkhagId(userProfileDto.getPresentDzongkhagId());
        userInfo.setPresentGeogId(userProfileDto.getPresentGeogId());
        iUserInfoRepository.save(userInfo);
        return ResponseEntity.ok(new MessageResponse("Current address information updated successfully."));
    }

    @Override
    public ResponseEntity<?> syncCensusRecord(UserProfileDto userProfileDto) throws ParseException {

        Optional<UserInfo> userInfoCheck = iUserInfoRepository.findByCid(userProfileDto.getCid());
        if (!userInfoCheck.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("User already exist having CID " + userProfileDto.getCid()));
        }
        UserInfo userInfo = iUserInfoRepository.findById(userProfileDto.getUserId()).get();
        Date dob = new SimpleDateFormat("dd/MM/yyyy").parse(userProfileDto.getBirthDate());
        userInfo.setCid(userProfileDto.getCid());
        userInfo.setDob(dob);
        userInfo.setFullName(userProfileDto.getFullName());
        userInfo.setGender(userProfileDto.getGender());
        userInfo.setFatherName(userProfileDto.getFatherName());
        userInfo.setMotherName(userProfileDto.getMotherName());
        userInfo.setPermanentPlaceName(userProfileDto.getPermanentPlaceName());
        userInfo.setPermanentGeog(userProfileDto.getPermanentGeog());
        userInfo.setPermanentDzongkhag(userProfileDto.getPermanentDzongkhag());

        iUserInfoRepository.save(userInfo);
        return ResponseEntity.ok(new MessageResponse("Census record updated successfully."));
    }

    @Override
    public ResponseEntity<?> searchUser(String searchKey) {
        Optional<UserInfo> userInfo;
        userInfo = iUserInfoRepository.findByCid(searchKey);
        if (!userInfo.isPresent()) {
            userInfo = iUserInfoRepository.findByEmail(searchKey);
        }
        if (!userInfo.isPresent()) {
            userInfo = iUserInfoRepository.findByUsername(searchKey);
        }
        if (userInfo.isPresent()) {
            return ResponseEntity.ok().body(userInfo.get());
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("User not found matching " + searchKey));
        }
    }

    @Override
    public ResponseEntity<?> changeProfilePic(HttpServletRequest request, UserProfileDto userProfileDto) throws IOException {

        MultipartFile profilePicture = userProfileDto.getProfilePicture();

        if (Objects.isNull(profilePicture)) {
            return ResponseEntity.badRequest().body(new MessageResponse("User does not have profile picture."));
        }
        String filename = profilePicture.getOriginalFilename();
        Long fileSize = profilePicture.getSize();
        String fileExtension = Objects.requireNonNull(filename).substring(filename.lastIndexOf(".") + 1, filename.length()).toUpperCase();

        FileUploadDTO fileUploadDTO = FileUploadToExternalLocation.fileUploadPathRetriever(request);
        String fileUrl = fileUploadDTO.getUploadFilePath().concat(filename);
        if (!filename.equals("")) {
            ResponseMessage responseMessage = FileUploadToExternalLocation.fileUploader(profilePicture, filename, "attachFile.properties", request);
            UserInfo userInfoDb = iUserInfoRepository.findById(userProfileDto.getUserId()).get();
            UserInfo userInfo = new ModelMapper().map(userInfoDb, UserInfo.class);
            userInfo.setProfilePictureName(filename);
            userInfo.setProfilePictureUrl(fileUrl);
            userInfo.setProfilePictureExt(fileExtension);
            userInfo.setProfilePictureSize(fileSize.toString());
            iUserInfoRepository.save(userInfo);
        }

        return ResponseEntity.ok(new MessageResponse("Profile changed successfully."));
    }


    private ResponseEntity<?> verifyOtp(UserProfileDto userProfileDto) {
        ChangeMobileNoSmsOtp changeMobileNoSmsOtp = iChangeMobileNoSmsOtpRepository.findById(userProfileDto.getUserId()).get();
        if (changeMobileNoSmsOtp == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("OTP not found matching mobile number " + userProfileDto.getMobileNo()));
        }
        if (Objects.equals(changeMobileNoSmsOtp.getOtp(), userProfileDto.getOtp())) {
            return ResponseEntity.ok("");
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("OTP didn't match."));
        }
    }


    @Override
    public ResponseEntity<?> getRegisteredUsers() {
        List<UserInfo> userInfos = iUserInfoRepository.findAll();
        return ResponseEntity.ok(userInfos);
    }

    @Override
    public ResponseEntity<?> getAllUsersEligibleForTraining(Date paramDate, Integer paramAge) {
        List<UserInfo> userInfos = iUserInfoRepository.getAllUsersEligibleForTraining(paramDate, paramAge);
        return ResponseEntity.ok(userInfos);
    }

    @Override
    public ResponseEntity<?> checkUnderAge(BigInteger userId, Date paramDate) {
        UserProfileDto userProfileDto = userDao.checkUnderAge(userId, paramDate);
        return ResponseEntity.ok(userProfileDto);
    }

    @Override
    public ResponseEntity<?> getProfileInfoByIds(List<BigInteger> userIds) {
        List<UserInfo> userInfos = iUserInfoRepository.findAllById(userIds);
        return ResponseEntity.ok(userInfos);
    }

    @Override
    public ResponseEntity<?> getUserInformationByPartialCid(String cid) {
        cid = cid.trim().equals("") ? null : cid;
        List<UserInfo> userInfos = iUserInfoRepository.findAllByCidStartsWith(cid);
        return ResponseEntity.ok(userInfos);
    }
}
