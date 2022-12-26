package com.microservice.erp.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dao.UserDao;
import com.microservice.erp.domain.dto.*;
import com.microservice.erp.domain.entities.*;
import com.microservice.erp.domain.helper.*;
import com.microservice.erp.domain.repositories.*;
import com.microservice.erp.services.iServices.IProfileService;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@AllArgsConstructor
public class ProfileService implements IProfileService {
    private final UserDao userDao;
    private final ISaUserRepository iSaUserRepository;
    private final IChangeMobileNoSmsOtpRepository iChangeMobileNoSmsOtpRepository;
    private final IChangeEmailVerificationCodeRepository iChangeEmailVerificationCodeRepository;

    private final PasswordEncoder encoder;
    private final BCryptPasswordEncoder passwordEncoder;
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvxyz0123456789";
    private final AddToQueue addToQueue;

    @Override
    public ResponseEntity<?> getProfileInfo(String authHeader, BigInteger userId) {
        SaUser saUser = iSaUserRepository.findById(userId).get();
        UserProfileDto userProfileDto = new ModelMapper().map(saUser, UserProfileDto.class);
        userProfileDto.setPassword(null);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<String> request = new HttpEntity<>(headers);
        if (saUser.getPresentGeogId() != null) {
            String geogUrl = "http://localhost:81/api/training/management/common/getGeogByGeogId?geogId=" + saUser.getPresentGeogId();
            ResponseEntity<GeogDto> geogResponse = restTemplate.exchange(geogUrl, HttpMethod.GET, request, GeogDto.class);
            userProfileDto.setPresentGeogName(Objects.requireNonNull(geogResponse.getBody()).getGeogName());
        }
        if (saUser.getPresentDzongkhagId() != null) {
            String dzongkhagUrl = "http://localhost:81/api/training/management/common/getDzongkhagByDzongkhagId?dzongkhagId=" + saUser.getPresentDzongkhagId();
            ResponseEntity<DzongkhagDto> dzongkhagResponse = restTemplate.exchange(dzongkhagUrl, HttpMethod.GET, request, DzongkhagDto.class);
            userProfileDto.setPresentDzongkhagName(Objects.requireNonNull(dzongkhagResponse.getBody()).getDzongkhagName());
        }
        return ResponseEntity.ok(userProfileDto);
    }

    @Override
    public ResponseEntity<?> getProfilePicture(BigInteger userId) throws IOException {
        SaUser saUser = iSaUserRepository.findById(userId).get();
        String profilePictureUrl = saUser.getProfilePictureUrl();
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
        Random random = new Random();
        int number = random.nextInt(9999);//max upto 9999
        String otp = String.format("%04d", number);

        String message = "Your OTP for Gyalsung System is " + otp;
        EventBus eventBusSms = EventBus.withId(null, null, null, message, null, userProfileDto.getMobileNo());
        addToQueue.addToQueue("sms", eventBusSms);
        ChangeMobileNoSmsOtp changeMobileNoSmsOtp = new ChangeMobileNoSmsOtp();
        changeMobileNoSmsOtp.setUserId(userProfileDto.getUserId());
        changeMobileNoSmsOtp.setMobileNo(userProfileDto.getMobileNo());
        changeMobileNoSmsOtp.setOtp(otp);
        iChangeMobileNoSmsOtpRepository.save(changeMobileNoSmsOtp);
        return ResponseEntity.ok(changeMobileNoSmsOtp);
    }

    @Override
    public ResponseEntity<?> changeUsername(UserProfileDto userProfileDto) {
        SaUser saUserDb = iSaUserRepository.findById(userProfileDto.getUserId()).get();

        ResponseEntity<?> checkUsernameExistOrNot = checkUsernameExistOrNot(userProfileDto.getUsername());
        if (checkUsernameExistOrNot.getStatusCode().value() != HttpStatus.OK.value()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username already in use."));
        }

        SaUser saUserObject = new ModelMapper().map(saUserDb, SaUser.class);

        saUserObject.setUsername(userProfileDto.getUsername());
        iSaUserRepository.save(saUserDb);
        return ResponseEntity.ok(new MessageResponse("Username updated successfully."));
    }

    @Override
    public ResponseEntity<?> checkEmailExistOrNot(String email) {
        SaUser saUserDb = iSaUserRepository.findByEmail(email);
        if (saUserDb != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email already in use."));
        } else {
            return ResponseEntity.ok(new MessageResponse("Email available."));
        }
    }

    @Override
    public ResponseEntity<?> changeMobileNo(UserProfileDto userProfileDto) {
        SaUser saUserDb = iSaUserRepository.findById(userProfileDto.getUserId()).get();


        //verify otp
        ResponseEntity<?> responseEntity = verifyOtp(userProfileDto);
        if (responseEntity.getStatusCode().value() != HttpStatus.OK.value()) {
            return ResponseEntity.badRequest().body(new MessageResponse("OTP didn't match."));
        }
        SaUser saUserObject = new ModelMapper().map(saUserDb, SaUser.class);

        saUserObject.setMobileNo(userProfileDto.getMobileNo());
        iSaUserRepository.save(saUserDb);
        return ResponseEntity.ok(new MessageResponse("Mobile number changed successfully."));
    }

    @Override
    public ResponseEntity<?> changeEmail(UserProfileDto userProfileDto) {

        SaUser saUserDb = iSaUserRepository.findById(userProfileDto.getUserId()).get();
        SaUser saUser = new ModelMapper().map(saUserDb, SaUser.class);

        //verify verification code
        ResponseEntity<?> responseEntity = verifyVerificationCode(userProfileDto);
        if (responseEntity.getStatusCode().value() != HttpStatus.OK.value()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Verification code didn't match."));
        }
        ResponseEntity<?> checkEmailExistOrNot = checkEmailExistOrNot(userProfileDto.getEmail());
        if (checkEmailExistOrNot.getStatusCode().value() != HttpStatus.OK.value()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email already in use."));
        }
        saUserDb.setEmail(userProfileDto.getEmail());
        iSaUserRepository.save(saUserDb);
        return ResponseEntity.ok(new MessageResponse("Email changed successfully."));
    }

    @Override
    public ResponseEntity<?> receiveEmailVcode(UserProfileDto userProfileDto) throws Exception {
        String verificationCode = generateVerificationCode(6);

        String subject = "Email verification";
        String message = "Dear, The verification code to change email for Gyalsung system is " + verificationCode;
        EventBus eventBusEmail = EventBus.withId(userProfileDto.getEmail(), null, null, message, subject, null);
        addToQueue.addToQueue("email", eventBusEmail);
        ChangeEmailVerificationCode changeEmailVerificationCode = new ChangeEmailVerificationCode();
        changeEmailVerificationCode.setUserId(userProfileDto.getUserId());
        changeEmailVerificationCode.setEmail(userProfileDto.getEmail());
        changeEmailVerificationCode.setVerificationCode(verificationCode);
        iChangeEmailVerificationCodeRepository.save(changeEmailVerificationCode);
        return ResponseEntity.ok(changeEmailVerificationCode);
    }

    @Override
    public ResponseEntity<?> changePassword(UserProfileDto userProfileDto) {
        SaUser saUserDb = iSaUserRepository.findById(userProfileDto.getUserId()).get();
        SaUser saUser = new ModelMapper().map(saUserDb, SaUser.class);

        //current pw must be equal to existing pw
//        TODO: need to check pw match
//        String curPw =  userProfileDto.getCurrentPassword();
//        if (!passwordEncoder.matches(saUserDb.getPassword(), curPw)) {
//            return ResponseEntity.badRequest().body(new MessageResponse("Current password doesn't match."));
//        }

        //confirm current pw must be equal to pw
        if (!Objects.equals(userProfileDto.getNewPassword(), userProfileDto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Confirm password doesn't match."));
        }

        saUser.setPassword(passwordEncoder.encode(userProfileDto.getNewPassword()));
        iSaUserRepository.save(saUser);
        //TODO: send email after changing password
        return ResponseEntity.ok(new MessageResponse("Password changed successfully."));
    }

    @Override
    public ResponseEntity<?> changeParentInfo(UserProfileDto userProfileDto) {
        SaUser saUser = iSaUserRepository.findById(userProfileDto.getUserId()).get();

        saUser.setFatherCid(userProfileDto.getFatherCid());
        saUser.setFatherMobileNo(userProfileDto.getFatherMobileNo());
        saUser.setFatherEmail(userProfileDto.getFatherEmail());
        saUser.setFatherOccupation(userProfileDto.getFatherOccupation());

        saUser.setMotherCid(userProfileDto.getFatherCid());
        saUser.setMotherMobileNo(userProfileDto.getMotherMobileNo());
        saUser.setMotherEmail(userProfileDto.getMotherEmail());
        saUser.setMotherOccupation(userProfileDto.getMotherOccupation());

        iSaUserRepository.save(saUser);
        return ResponseEntity.ok(new MessageResponse("Parent information updated successfully."));
    }

    @Override
    public ResponseEntity<?> changeGuardianInfo(UserProfileDto userProfileDto) {
        SaUser saUser = iSaUserRepository.findById(userProfileDto.getUserId()).get();

        saUser.setGuardianName(userProfileDto.getGuardianName());
        saUser.setGuardianCid(userProfileDto.getGuardianCid());
        saUser.setGuardianOccupation(userProfileDto.getGuardianOccupation());
        saUser.setGuardianMobileNo(userProfileDto.getGuardianMobileNo());
        saUser.setGuardianEmail(userProfileDto.getGuardianEmail());
        saUser.setRelationToGuardian(userProfileDto.getRelationToGuardian());

        iSaUserRepository.save(saUser);
        return ResponseEntity.ok(new MessageResponse("Guardian information updated successfully."));
    }

    @Override
    public ResponseEntity<?> changeSocialMediaLink(UserProfileDto userProfileDto) {
        SaUser saUser = iSaUserRepository.findById(userProfileDto.getUserId()).get();
        saUser.setSocialMediaLink1(userProfileDto.getSocialMediaLink1());
        saUser.setSocialMediaLink2(userProfileDto.getSocialMediaLink2());
        saUser.setSocialMediaLink3(userProfileDto.getSocialMediaLink3());
        iSaUserRepository.save(saUser);
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
    public ResponseEntity<?> getAllDzongkhags(String authHeader) {
        List<DzongkhagDto> dzongkhagDtos = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<String> request = new HttpEntity<>(headers);
        String url = "http://localhost:81/api/training/management/common/getAllDzongkhags";
        ResponseEntity<DzongkhagDto[]> response = restTemplate.exchange(url, HttpMethod.GET, request, DzongkhagDto[].class);
        for (DzongkhagDto dzongkhagDto : response.getBody()) {
            dzongkhagDtos.add(dzongkhagDto);
        }
        return ResponseEntity.ok(dzongkhagDtos);
    }

    @Override
    public ResponseEntity<?> getGeogByDzongkhagId(String authHeader, Integer dzongkhagId) {
        List<GeogDto> geogDtos = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<String> request = new HttpEntity<>(headers);
        String url = "http://localhost:81/api/training/management/common/getGeogByDzongkhagId?dzongkhagId=" + dzongkhagId;
        ResponseEntity<GeogDto[]> response = restTemplate.exchange(url, HttpMethod.GET, request, GeogDto[].class);
        for (GeogDto geogDto : response.getBody()) {
            geogDtos.add(geogDto);
        }
        return ResponseEntity.ok(geogDtos);
    }

    @Override
    public ResponseEntity<?> changeCurrentAddress(UserProfileDto userProfileDto) {
        SaUser saUser = iSaUserRepository.findById(userProfileDto.getUserId()).get();
        saUser.setPresentCountry(userProfileDto.getPresentCountry());
        saUser.setPresentPlaceName(userProfileDto.getPresentPlaceName());
        saUser.setPresentDzongkhagId(userProfileDto.getPresentDzongkhagId());
        saUser.setPresentGeogId(userProfileDto.getPresentGeogId());
        iSaUserRepository.save(saUser);
        return ResponseEntity.ok(new MessageResponse("Guardian information updated successfully."));
    }

    @Override
    public ResponseEntity<?> syncCensusRecord(UserProfileDto userProfileDto) throws ParseException {

        SaUser saUserCheck = iSaUserRepository.findByCid(userProfileDto.getCid());
        if (saUserCheck != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("User already exist having CID " + userProfileDto.getCid()));
        }
        SaUser saUser = iSaUserRepository.findById(userProfileDto.getUserId()).get();
        Date dob = new SimpleDateFormat("dd/MM/yyyy").parse(userProfileDto.getBirthDate());
        saUser.setCid(userProfileDto.getCid());
        saUser.setDob(dob);
        saUser.setFullName(userProfileDto.getFullName());
        saUser.setGender(userProfileDto.getGender());
//        saUser.setSex(userProfileDto.getSex().toUpperCase());
        saUser.setFatherName(userProfileDto.getFatherName());
        saUser.setMotherName(userProfileDto.getMotherName());
        saUser.setPermanentPlaceName(userProfileDto.getPermanentPlaceName());
        saUser.setPermanentGeog(userProfileDto.getPermanentGeog());
        saUser.setPermanentDzongkhag(userProfileDto.getPermanentDzongkhag());

        iSaUserRepository.save(saUser);
        return ResponseEntity.ok(new MessageResponse("Census record updated successfully."));
    }

    @Override
    public ResponseEntity<?> searchUser(String searchKey) {
        SaUser saUser;
        saUser = iSaUserRepository.findByCid(searchKey);
        if (saUser == null) {
            saUser = iSaUserRepository.findByEmail(searchKey);
        }
        if (saUser == null) {
            saUser = iSaUserRepository.findByUsername(searchKey);
        }
        if (saUser != null) {
            return ResponseEntity.ok(saUser);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("User not found matching " + searchKey));
        }
    }

    @Override
    public ResponseEntity<?> resetUserPassword(UserProfileDto userProfileDto) {
        SaUser saUserDb = iSaUserRepository.findById(userProfileDto.getUserId()).get();
        SaUser saUser = new ModelMapper().map(saUserDb, SaUser.class);

        //confirm current pw must be equal to new pw
        if (!Objects.equals(userProfileDto.getNewPassword(), userProfileDto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Confirm password doesn't match."));
        }

        saUser.setPassword(passwordEncoder.encode(userProfileDto.getNewPassword()));
        iSaUserRepository.save(saUser);

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

            SaUser saUserDb = iSaUserRepository.findById(userProfileDto.getUserId()).get();
            SaUser saUser = new ModelMapper().map(saUserDb, SaUser.class);
            saUser.setProfilePictureName(filename);
            saUser.setProfilePictureUrl(fileUrl);
            saUser.setProfilePictureExt(fileExtension);
            saUser.setProfilePictureSize(fileSize.toString());
            iSaUserRepository.save(saUser);
        }

        return ResponseEntity.ok(new MessageResponse("Profile changed successfully."));
    }

    private ResponseEntity<?> checkUsernameExistOrNot(String username) {
        SaUser saUserDb = iSaUserRepository.findByUsername(username);
        if (saUserDb != null) {
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

    @Override
    public ResponseEntity<?> getRegisteredUsers() {
        List<SaUser> saUsers = iSaUserRepository.findAll();
        return ResponseEntity.ok(saUsers);
    }

    @Override
    public ResponseEntity<?> getAllUsersEligibleForTraining(Date paramDate, Integer paramAge) {
        List<SaUser> saUsers = iSaUserRepository.getAllUsersEligibleForTraining(paramDate, paramAge);
        return ResponseEntity.ok(saUsers);
    }

    @Override
    public ResponseEntity<?> checkUnderAge(BigInteger userId, Date paramDate) {
        UserProfileDto userProfileDto = userDao.checkUnderAge(userId, paramDate);
        return ResponseEntity.ok(userProfileDto);
    }

}
