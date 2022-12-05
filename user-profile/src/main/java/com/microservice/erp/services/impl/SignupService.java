package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.SignupRequestDto;
import com.microservice.erp.domain.entities.*;
import com.microservice.erp.domain.helper.MailSender;
import com.microservice.erp.domain.helper.SmsSender;
import com.microservice.erp.domain.repositories.ISaRoleRepository;
import com.microservice.erp.domain.repositories.ISignupEmailVerificationCodeRepository;
import com.microservice.erp.domain.repositories.ISignupSmsOtpRepository;
import com.microservice.erp.domain.dto.CitizenDetailDto;
import com.microservice.erp.domain.dto.NotificationRequestDto;
import com.microservice.erp.domain.dto.MessageResponse;
import com.microservice.erp.domain.repositories.ISaUserRepository;
import com.microservice.erp.services.iServices.ISignupService;
import com.squareup.okhttp.OkHttpClient;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.wso2.client.api.ApiClient;
import org.wso2.client.api.ApiException;
import org.wso2.client.api.DCRC_CitizenDetailsAPI.DefaultApi;
import org.wso2.client.model.DCRC_CitizenDetailsAPI.CitizenDetailsResponse;
import org.wso2.client.model.DCRC_CitizenDetailsAPI.CitizendetailsObj;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class SignupService implements ISignupService {
    private final CitizenDetailApiService citizenDetailApiService;
    private final ISignupSmsOtpRepository iSignupSmsOtpRepository;
    private final ISignupEmailVerificationCodeRepository iSignupEmailVerificationCodeRepository;
    private final ISaUserRepository iSaUserRepository;
    private final ISaRoleRepository iSaRoleRepository;

    private final PasswordEncoder encoder;
    private final BCryptPasswordEncoder passwordEncoder;

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvxyz0123456789";


    @Override
    public ResponseEntity<?> getCitizenDetails(String cid, String dob) throws ParseException, IOException, ApiException {
        return validateCitizenDetails(cid, dob);
    }

    @Override
    public ResponseEntity<?> receiveOtp(NotificationRequestDto notificationRequestDto) {
        Random random = new Random();
        int number = random.nextInt(9999);//max upto 9999
        String otp = String.format("%04d", number);
        String message = "Your OTP for Gyalsung Registration is " + otp;
        SignupSmsOtp signupSmsOtp = new SignupSmsOtp();
        signupSmsOtp.setMobileNo(notificationRequestDto.getMobileNo());
        signupSmsOtp.setOtp(otp);
        iSignupSmsOtpRepository.save(signupSmsOtp);
        SmsSender.sendSms(notificationRequestDto.getMobileNo(), message);
        return ResponseEntity.ok(signupSmsOtp);
    }

    @Override
    public ResponseEntity<?> verifyOtp(NotificationRequestDto notificationRequestDto) {
        SignupSmsOtp signupSmsOtp = iSignupSmsOtpRepository.findByMobileNo(notificationRequestDto.getMobileNo());
        if (signupSmsOtp == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("OTP not found matching mobile number " + notificationRequestDto.getMobileNo()));
        }
        if (Objects.equals(signupSmsOtp.getOtp(), notificationRequestDto.getOtp())) {
            return ResponseEntity.ok("");
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("OTP didn't match."));
        }
    }

    @Override
    public ResponseEntity<?> receiveEmailVcode(NotificationRequestDto notificationRequestDto) throws Exception {
        SaUser saUserDB = iSaUserRepository.findByEmail(notificationRequestDto.getEmail());
        if (saUserDB != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email already in use."));
        }
        String verificationCode = generateVerificationCode(6);

        String subject = "Email verification";
        String message = "Dear, The verification code for Gyalsung system is " + verificationCode;
        MailSender.sendMail(notificationRequestDto.getEmail(), null, null, message, subject);

        SignupEmailVerificationCode signupEmailVerificationCode = new SignupEmailVerificationCode();
        signupEmailVerificationCode.setEmail(notificationRequestDto.getEmail());
        signupEmailVerificationCode.setVerificationCode(verificationCode);
//        iSignupEmailVerificationCodeRepository.deleteById(notificationRequestDto.getEmail());
        iSignupEmailVerificationCodeRepository.save(signupEmailVerificationCode);
        return ResponseEntity.ok(signupEmailVerificationCode);
    }

    @Override
    public ResponseEntity<?> verifyEmailVcode(NotificationRequestDto notificationRequestDto) {
        SignupEmailVerificationCode signupEmailVerificationCode = iSignupEmailVerificationCodeRepository.findByEmail(notificationRequestDto.getEmail());

        if (signupEmailVerificationCode == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Verification code not found matching associated with email " + notificationRequestDto.getEmail()));
        }

        if (Objects.equals(signupEmailVerificationCode.getVerificationCode(), notificationRequestDto.getVerificationCode())) {
            return ResponseEntity.ok("");
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Email verification code didn't match."));
        }
    }

    @Override
    public ResponseEntity<?> signup(SignupRequestDto signupRequestDto) throws ParseException {
        //check already registered not by CID
        SaUser saUserDB = iSaUserRepository.findByCid(signupRequestDto.getCid());
        if (saUserDB != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("User with CID " + signupRequestDto.getCid() + " already exist."));
        }
        //Mobile number verification OTP received from dto must be equal to backend
        NotificationRequestDto notificationRequestDto = new NotificationRequestDto();
        notificationRequestDto.setMobileNo(signupRequestDto.getMobileNo());
        notificationRequestDto.setOtp(signupRequestDto.getOtp());
        ResponseEntity<?> responseEntity = verifyOtp(notificationRequestDto);
        if (responseEntity.getStatusCode().value() != HttpStatus.OK.value()) {
            return ResponseEntity.badRequest().body(new MessageResponse("OTP didn't match."));
        }

        //Email verification code received from dto must be equal to backend
        notificationRequestDto.setEmail(signupRequestDto.getEmail());
        notificationRequestDto.setVerificationCode(signupRequestDto.getVerificationCode());
        ResponseEntity<?> responseEntityEmail = verifyEmailVcode(notificationRequestDto);
        if (responseEntityEmail.getStatusCode().value() != HttpStatus.OK.value()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email verification code didn't match."));
        }

        //To check if the email is already in use or not
        SaUser saUserEmail = iSaUserRepository.findByEmail(signupRequestDto.getEmail());
        if (saUserEmail != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email already in use."));
        }
        //Password must be equal to confirm password
        if (!Objects.equals(signupRequestDto.getPassword(), signupRequestDto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Password didn't match."));
        }

        Date birthDate = new SimpleDateFormat("dd/MM/yyyy").parse(signupRequestDto.getBirthDate());
        signupRequestDto.setDob(birthDate);
        SaUser saUser = new ModelMapper().map(signupRequestDto, SaUser.class);
        saUser.setStatus('A');
        saUser.setSignupUser('Y');
        saUser.setUsername(signupRequestDto.getCid());
        saUser.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
        //todo:set role equal to USER
        Set<SaRole> saRoles = new HashSet<>();
        SaRole saRoleDb = iSaRoleRepository.findByRoleId(1);//todo:need to get student user role information
        saRoles.add(saRoleDb);
        saUser.setSaRoles(saRoles);
        iSaUserRepository.save(saUser);
        return ResponseEntity.ok(new MessageResponse("Registered successfully."));
    }

    public static String generateVerificationCode(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    private ResponseEntity<?> validateCitizenDetails(String cid, String dob) throws ParseException, ApiException, IOException {
        CitizenDetailDto citizenDetailDto = new CitizenDetailDto();

        Resource resource = new ClassPathResource("/apiConfig/dcrcApi.properties");
        Properties props = PropertiesLoaderUtils.loadProperties(resource);
        String getCitizenDetails = props.getProperty("getCitizenDetails.endPointURL");

//        ResourceBundle resourceBundle = ResourceBundle.getBundle("/apiConfig/wsEndPointURL_en_US");
//        String getCitizenDetails = resourceBundle.getString("getCitizenDetails.endPointURL");

        OkHttpClient httpClient = new OkHttpClient();
        httpClient.setConnectTimeout(10000, TimeUnit.MILLISECONDS);
        httpClient.setReadTimeout(10000, TimeUnit.MILLISECONDS);

        ApiClient apiClient = new ApiClient();
        apiClient.setHttpClient(httpClient);

        apiClient.setBasePath(getCitizenDetails);
        //for stagging
//            apiClient.setAccessToken("75354085-5182-36f3-a98d-5548dbec5303");

        //region off this in stagging
        ApiAccessToken apiAccessToken = citizenDetailApiService.getApplicationToken();
        apiClient.setAccessToken(apiAccessToken.getAccess_token());
        //endregion

        DefaultApi api = new DefaultApi(apiClient);
        CitizenDetailsResponse citizenDetailsResponse = api.citizendetailsCidGet(cid);
        if (citizenDetailsResponse.getCitizenDetailsResponse().getCitizenDetail() != null && !citizenDetailsResponse.getCitizenDetailsResponse().getCitizenDetail().isEmpty()) {
            CitizendetailsObj citizendetailsObj = citizenDetailsResponse.getCitizenDetailsResponse().getCitizenDetail().get(0);

            char genderChar = citizendetailsObj.getGender().charAt(0);
            String genderName = "Male";
            if (genderChar == 'F') {
                genderName = "Female";
            } else if (genderChar == 'O') {
                genderName = "Other";
            }

            String censusDob = citizendetailsObj.getDob();
//            Date citizenDob = new SimpleDateFormat("dd/MM/yyyy").parse(censusDob);

            if (!censusDob.equals(dob)) {
                return ResponseEntity.badRequest().body(new MessageResponse("CID and date of birth did not matched."));
            }
            citizenDetailDto.setFullName(citizendetailsObj.getFirstName() + " " + citizendetailsObj.getMiddleName() + " " + citizendetailsObj.getLastName());
            citizenDetailDto.setFullName(citizenDetailDto.getFullName().replaceAll("null", ""));
            citizenDetailDto.setCid(citizendetailsObj.getCid());
            citizenDetailDto.setDob(censusDob);
            citizenDetailDto.setGender(genderChar);
            citizenDetailDto.setGenderName(genderName);
            citizenDetailDto.setFatherName(citizendetailsObj.getFatherName());
            citizenDetailDto.setFatherCid(null);
            citizenDetailDto.setMotherName(citizendetailsObj.getMotherName());
            citizenDetailDto.setMotherCid(null);
            citizenDetailDto.setVillageName(citizendetailsObj.getVillageName());
            citizenDetailDto.setGeogName(citizendetailsObj.getGewogName());
            citizenDetailDto.setDzongkhagName(citizendetailsObj.getDzongkhagName());
            citizenDetailDto.setHouseNo(citizendetailsObj.getHouseNo());
            citizenDetailDto.setThramNo(citizendetailsObj.getThramNo());

        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("No information found matching CID No " + cid));
        }
        return ResponseEntity.ok(citizenDetailDto);
    }


}
