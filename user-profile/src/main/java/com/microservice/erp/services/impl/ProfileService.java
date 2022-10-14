package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.MessageResponse;
import com.microservice.erp.domain.dto.UserProfileDto;
import com.microservice.erp.domain.entities.*;
import com.microservice.erp.domain.helper.FileUploadDTO;
import com.microservice.erp.domain.helper.FileUploadToExternalLocation;
import com.microservice.erp.domain.helper.ResponseMessage;
import com.microservice.erp.domain.helper.SystemDataInt;
import com.microservice.erp.domain.repositories.*;
import com.microservice.erp.services.EmailSenderService;
import com.microservice.erp.services.iServices.IProfileService;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@AllArgsConstructor
public class ProfileService implements IProfileService {
    private final IUserInfoRepository iUserInfoRepository;
    private final IChangeMobileNoSmsOtpRepository iChangeMobileNoSmsOtpRepository;
    private final IChangeEmailVerificationCodeRepository iChangeEmailVerificationCodeRepository;
    private final EmailSenderService emailSenderService;

    private final PasswordEncoder encoder;
    private final BCryptPasswordEncoder passwordEncoder;

    private final IDzongkhagRepository dzongkhagRepository;
    private final IGeogRepository geogRepository;
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvxyz0123456789";


    @Override
    public ResponseEntity<?> getProfileInfo(Long userId) {
        UserInfo userInfo = iUserInfoRepository.findById(userId).get();
        return ResponseEntity.ok(userInfo);
    }

    @Override
    public ResponseEntity<?> getProfilePicture(Long userId) throws IOException {
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

    public ResponseEntity<?> receiveOtp(UserProfileDto userProfileDto) {
        Random random = new Random();
        int number = random.nextInt(9999);//max upto 9999
        String otp = String.format("%04d", number);

        String message = "Your OTP for Gyalsung System is " + otp;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange("http://172.30.16.213/g2csms/push.php?to=" + userProfileDto.getMobileNo() + "&msg=" + message, HttpMethod.GET, null, String.class);
        ChangeMobileNoSmsOtp changeMobileNoSmsOtp = new ChangeMobileNoSmsOtp();
        changeMobileNoSmsOtp.setUserId(userProfileDto.getUserId());
        changeMobileNoSmsOtp.setMobileNo(userProfileDto.getMobileNo());
        changeMobileNoSmsOtp.setOtp(otp);
        iChangeMobileNoSmsOtpRepository.save(changeMobileNoSmsOtp);
        return ResponseEntity.ok(changeMobileNoSmsOtp);
    }

    @Override
    public ResponseEntity<?> changeUsername(UserProfileDto userProfileDto) {
        UserInfo userInfoDb = iUserInfoRepository.findById(userProfileDto.getUserId()).get();

        ResponseEntity<?> checkUsernameExistOrNot = checkUsernameExistOrNot(userProfileDto.getUsername());
        if (checkUsernameExistOrNot.getStatusCode().value() != HttpStatus.OK.value()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username already in use."));
        }

        UserInfo userInfoObject = new ModelMapper().map(userInfoDb, UserInfo.class);

        userInfoObject.setUsername(userProfileDto.getUsername());
        iUserInfoRepository.save(userInfoDb);
        return ResponseEntity.ok(new MessageResponse("Username updated successfully."));
    }

    @Override
    public ResponseEntity<?> checkEmailExistOrNot(String email) {
        UserInfo userInfoDb = iUserInfoRepository.findByEmail(email);
        if (userInfoDb != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email already in use."));
        } else {
            return ResponseEntity.ok(new MessageResponse("Email available."));
        }
    }

    @Override
    public ResponseEntity<?> changeMobileNo(UserProfileDto userProfileDto) {
        UserInfo userInfoDb = iUserInfoRepository.findById(userProfileDto.getUserId()).get();


        //verify otp
        ResponseEntity<?> responseEntity = verifyOtp(userProfileDto);
        if (responseEntity.getStatusCode().value() != HttpStatus.OK.value()) {
            return ResponseEntity.badRequest().body(new MessageResponse("OTP didn't match."));
        }
        UserInfo userInfoObject = new ModelMapper().map(userInfoDb, UserInfo.class);

        userInfoObject.setMobileNo(userProfileDto.getMobileNo());
        iUserInfoRepository.save(userInfoDb);
        return ResponseEntity.ok(new MessageResponse("Mobile number changed successfully."));
    }

    @Override
    public ResponseEntity<?> changeEmail(UserProfileDto userProfileDto) {

        UserInfo userInfoDb = iUserInfoRepository.findById(userProfileDto.getUserId()).get();
        UserInfo userInfo = new ModelMapper().map(userInfoDb, UserInfo.class);

        //verify verification code
        ResponseEntity<?> responseEntity = verifyVerificationCode(userProfileDto);
        if (responseEntity.getStatusCode().value() != HttpStatus.OK.value()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Verification code didn't match."));
        }
        ResponseEntity<?> checkEmailExistOrNot = checkEmailExistOrNot(userProfileDto.getEmail());
        if (checkEmailExistOrNot.getStatusCode().value() != HttpStatus.OK.value()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email already in use."));
        }
        userInfoDb.setEmail(userProfileDto.getEmail());
        iUserInfoRepository.save(userInfoDb);
        return ResponseEntity.ok(new MessageResponse("Email changed successfully."));
    }

    @Override
    public ResponseEntity<?> receiveEmailVcode(UserProfileDto userProfileDto) {
        String verificationCode = generateVerificationCode(6);

        emailSenderService.sendSimpleEmail(userProfileDto.getEmail(), "Email verification", "Dear, The verification code to change email for Gyalsung system is " + verificationCode);

        ChangeEmailVerificationCode changeEmailVerificationCode = new ChangeEmailVerificationCode();
        changeEmailVerificationCode.setUserId(userProfileDto.getUserId());
        changeEmailVerificationCode.setEmail(userProfileDto.getEmail());
        changeEmailVerificationCode.setVerificationCode(verificationCode);
        iChangeEmailVerificationCodeRepository.save(changeEmailVerificationCode);
        return ResponseEntity.ok(changeEmailVerificationCode);
    }

    @Override
    public ResponseEntity<?> changePassword(UserProfileDto userProfileDto) {
        UserInfo userInfoDb = iUserInfoRepository.findById(userProfileDto.getUserId()).get();
        UserInfo userInfo = new ModelMapper().map(userInfoDb, UserInfo.class);

        //current pw must be equal to existing pw
//        TODO: need to check pw match
//        String curPw =  userProfileDto.getCurrentPassword();
//        if (!passwordEncoder.matches(userInfoDb.getPassword(), curPw)) {
//            return ResponseEntity.badRequest().body(new MessageResponse("Current password doesn't match."));
//        }

        //confirm current pw must be equal to pw
        if (!Objects.equals(userProfileDto.getNewPassword(), userProfileDto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Confirm password doesn't match."));
        }

        userInfo.setPassword(passwordEncoder.encode(userProfileDto.getNewPassword()));
        iUserInfoRepository.save(userInfo);
        //TODO: send email after cahnging password
        return ResponseEntity.ok(new MessageResponse("Password changed successfully."));
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

        userInfo.setGuardianName(userProfileDto.getGuardianName());
        userInfo.setGuardianCid(userProfileDto.getGuardianCid());
        userInfo.setGuardianOccupation(userProfileDto.getGuardianOccupation());
        userInfo.setGuardianMobileNo(userProfileDto.getGuardianMobileNo());
        userInfo.setGuardianEmail(userProfileDto.getGuardianEmail());
        userInfo.setRelationToGuardian(userProfileDto.getRelationToGuardian());

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
        return ResponseEntity.ok(new MessageResponse("Guardian information updated successfully."));

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
    public ResponseEntity<?> getAllDzongkhags() {
        List<Dzongkhag> dzongkhags = dzongkhagRepository.findAll();
        return ResponseEntity.ok(dzongkhags);
    }

    @Override
    public ResponseEntity<?> getGeogByDzongkhagId(Integer dzongkhagId) {
        List<Geog> geogs = geogRepository.findByDzongkhagId(dzongkhagId);
        return ResponseEntity.ok(geogs);
    }

    @Override
    public ResponseEntity<?> changeCurrentAddress(UserProfileDto userProfileDto) {
        UserInfo userInfo = iUserInfoRepository.findById(userProfileDto.getUserId()).get();
        userInfo.setPresentCountry(userProfileDto.getPresentCountry());
        userInfo.setPresentPlaceName(userProfileDto.getPresentPlaceName());

        Dzongkhag dzongkhag = dzongkhagRepository.findByDzongkhagId(userProfileDto.getPresentDzongkhagId());
        Geog geog = geogRepository.findByGeogId(userProfileDto.getPresentGeogId());
        userInfo.setPresentDzongkhag(new Dzongkhag(userProfileDto.getPresentDzongkhagId(), dzongkhag.getDzongkhagName()));
        userInfo.setPresentGeog(new Geog(userProfileDto.getPresentGeogId(), geog.getDzongkhagId(), geog.getGeogName()));

        iUserInfoRepository.save(userInfo);
        return ResponseEntity.ok(new MessageResponse("Guardian information updated successfully."));
    }

    @Override
    public ResponseEntity<?> syncCensusRecord(UserProfileDto userProfileDto) throws ParseException {

        UserInfo userInfoCheck = iUserInfoRepository.findByCid(userProfileDto.getCid());
        if (userInfoCheck != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("User already exist having CID " + userProfileDto.getCid()));
        }
        UserInfo userInfo = iUserInfoRepository.findById(userProfileDto.getUserId()).get();
        Date dob = new SimpleDateFormat("dd/MM/yyyy").parse(userProfileDto.getBirthDate());
        userInfo.setCid(userProfileDto.getCid());
        userInfo.setDob(dob);
        userInfo.setFullName(userProfileDto.getFullName());
        userInfo.setSex(userProfileDto.getSex().toUpperCase());
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
        UserInfo userInfo;
        userInfo = iUserInfoRepository.findByCid(searchKey);
        if (userInfo == null) {
            userInfo = iUserInfoRepository.findByEmail(searchKey);
        }
        if (userInfo == null) {
            userInfo = iUserInfoRepository.findByUsername(searchKey);
        }
        if (userInfo != null) {
            return ResponseEntity.ok(userInfo);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("User not found matching " + searchKey));
        }
    }

    @Override
    public ResponseEntity<?> resetUserPassword(UserProfileDto userProfileDto) {
        UserInfo userInfoDb = iUserInfoRepository.findById(userProfileDto.getUserId()).get();
        UserInfo userInfo = new ModelMapper().map(userInfoDb, UserInfo.class);

        //confirm current pw must be equal to new pw
        if (!Objects.equals(userProfileDto.getNewPassword(), userProfileDto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Confirm password doesn't match."));
        }

        userInfo.setPassword(passwordEncoder.encode(userProfileDto.getNewPassword()));
        iUserInfoRepository.save(userInfo);

        //TODO: send email after resetting password
        return ResponseEntity.ok(new MessageResponse("Password changed successfully."));
    }

    @Override
    public ResponseEntity<?> changeProfilePic(HttpServletRequest request, UserProfileDto userProfileDto) throws IOException {

        MultipartFile profilePicture = userProfileDto.getProfilePicture();

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

    private ResponseEntity<?> checkUsernameExistOrNot(String username) {
        UserInfo userInfoDb = iUserInfoRepository.findByUsername(username);
        if (userInfoDb != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username already in use."));
        } else {
            return ResponseEntity.ok(new MessageResponse("Username available."));
        }
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

    public static String generateVerificationCode(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
}
