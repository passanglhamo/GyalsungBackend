package com.microservice.erp.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.erp.domain.dto.*;
import com.microservice.erp.domain.entities.ApiAccessToken;
import com.microservice.erp.domain.entities.SignupEmailVerificationCode;
import com.microservice.erp.domain.entities.SignupSmsOtp;
import com.microservice.erp.domain.entities.UserInfo;
import com.microservice.erp.domain.repositories.ISignupEmailVerificationCodeRepository;
import com.microservice.erp.domain.repositories.ISignupSmsOtpRepository;
import com.microservice.erp.domain.repositories.IUserInfoRepository;
import com.microservice.erp.services.iServices.ISignupService;
import com.squareup.okhttp.OkHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.wso2.client.api.ApiClient;
import org.wso2.client.api.ApiException;
import org.wso2.client.api.DCRC_CitizenDetailsAPI.DefaultApi;
import org.wso2.client.model.DCRC_CitizenDetailsAPI.CitizenDetailsResponse;
import org.wso2.client.model.DCRC_CitizenDetailsAPI.CitizendetailsObj;
import org.wso2.client.model.DCRC_CitizenDetailsAPI.ParentdetailObj;
import org.wso2.client.model.DCRC_CitizenDetailsAPI.ParentdetailResponse;

import javax.json.JsonArray;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
//@AllArgsConstructor
public class SignupService implements ISignupService {
    private final CitizenDetailApiService citizenDetailApiService;
    private final ISignupSmsOtpRepository iSignupSmsOtpRepository;
    private final ISignupEmailVerificationCodeRepository iSignupEmailVerificationCodeRepository;
    private final IUserInfoRepository iUserInfoRepository;

    private final AddToQueue addToQueue;
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvxyz0123456789";

    public SignupService(CitizenDetailApiService citizenDetailApiService, ISignupSmsOtpRepository iSignupSmsOtpRepository, ISignupEmailVerificationCodeRepository iSignupEmailVerificationCodeRepository, IUserInfoRepository iUserInfoRepository, AddToQueue addToQueue) {
        this.citizenDetailApiService = citizenDetailApiService;
        this.iSignupSmsOtpRepository = iSignupSmsOtpRepository;
        this.iSignupEmailVerificationCodeRepository = iSignupEmailVerificationCodeRepository;
        this.iUserInfoRepository = iUserInfoRepository;
        this.addToQueue = addToQueue;
    }


    @Override
    public ResponseEntity<?> getCitizenDetails(String cid, String dob) throws ParseException, IOException, ApiException {
        return validateCitizenDetails(cid, dob);
    }

    @Override
    public ResponseEntity<?> receiveOtp(NotificationRequestDto notificationRequestDto) throws JsonProcessingException {
        Random random = new Random();
        int number = random.nextInt(9999);//max upto 9999
        String otp = String.format("%04d", number);
        String message = "Your OTP for Gyalsung Registration is " + otp;
        SignupSmsOtp signupSmsOtp = new SignupSmsOtp();
        signupSmsOtp.setMobileNo(notificationRequestDto.getMobileNo());
        signupSmsOtp.setOtp(otp);
        iSignupSmsOtpRepository.save(signupSmsOtp);

        EventBus eventBusSms = EventBus.withId(null, null, null, message, null, notificationRequestDto.getMobileNo());

        //todo get value from properties
        addToQueue.addToQueue("sms", eventBusSms);
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
        Optional<UserInfo> userInfoDB = iUserInfoRepository.findByEmail(notificationRequestDto.getEmail());
        if (userInfoDB.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email already in use."));
        }
        String verificationCode = generateVerificationCode(6);

        String subject = "Email verification";
        String message = "Dear, Your verification code for Gyalsung system is " + verificationCode;

        EventBus eventBusEmail = EventBus.withId(notificationRequestDto.getEmail(), null, null, message, subject, null);
        //todo get value from properties
        addToQueue.addToQueue("email", eventBusEmail);

        SignupEmailVerificationCode signupEmailVerificationCode = new SignupEmailVerificationCode();
        signupEmailVerificationCode.setEmail(notificationRequestDto.getEmail());
        signupEmailVerificationCode.setVerificationCode(verificationCode);
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
    public ResponseEntity<?> signup(SignupRequestDto signupRequestDto) throws ParseException, JsonProcessingException {

        //check already registered not by CID
        Optional<UserInfo> userInfoDB = iUserInfoRepository.findByCid(signupRequestDto.getCid());
        if (userInfoDB.isPresent()) {
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
        Optional<UserInfo> userInfoEmail = iUserInfoRepository.findByEmail(signupRequestDto.getEmail());
        if (userInfoEmail.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email already in use."));
        }
        //Password must be equal to confirm password
        if (!Objects.equals(signupRequestDto.getPassword(), signupRequestDto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Password didn't match."));
        }

        Date birthDate = new SimpleDateFormat("dd/MM/yyyy").parse(signupRequestDto.getBirthDate());
        signupRequestDto.setDob(birthDate);
        UserInfo userInfo = new ModelMapper().map(signupRequestDto, UserInfo.class);
        userInfo.setSignupUser('Y');
        userInfo.setUsername(signupRequestDto.getCid());

        //Adding data whethere person is monk/nun/student/dropout
        userInfo.setPersonStaId(signupRequestDto.getPersonStaId());

        //Adding present address (Country)
        userInfo.setPresentCountry(signupRequestDto.getPresentCountry());

        BigInteger userId = iUserInfoRepository.save(userInfo).getId();
//        todo: add to queue following data: password, roles, email, username, userId

        EventBusUser eventBusSms = EventBusUser.withId(userId, 'A', userInfo.getCid(), userInfo.getEmail()
                , userInfo.getUsername(), signupRequestDto.getPassword(), userInfo.getSignupUser(), null);
        addToQueue.addToUserQueue("addUser", eventBusSms);
        return ResponseEntity.ok(new MessageResponse("Registered successfully."));
    }

    @Override
    public ResponseEntity<?> getExpectedPopulationByYear(String dateString) throws IOException, ParseException {
        Resource resource = new ClassPathResource("/apiConfig/dcrcApi.properties");
        Properties props = PropertiesLoaderUtils.loadProperties(resource);
        String getExpectedUserDetails = props.getProperty("getExpectedUserDetails.endPointURL");
        //todo need to get age from properties file
        String userUrl = getExpectedUserDetails + "/" + dateString + "/18";
        URL url = new URL(userUrl);
        ObjectMapper mapper = new ObjectMapper();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        ApiAccessToken apiAccessToken = citizenDetailApiService.getApplicationToken();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + apiAccessToken.getAccess_token());


        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONArray details = new JSONObject(response.toString()).getJSONObject("details").getJSONArray("detail");
            List<JSONObject> detailsList = toList(details);
            List<ExpectedPopulationDto> expectedList = new ArrayList<>();
            detailsList.forEach(item->{
                ExpectedPopulationDto expectedPopulationDto = new ExpectedPopulationDto();
                String cidNo = item.getString("cidNo");
                expectedPopulationDto.setCidNo(cidNo);
                expectedPopulationDto.setDob(item.getString("dob"));
                expectedPopulationDto.setName(item.getString("name"));
                expectedPopulationDto.setGender(item.getString("gender"));
                expectedPopulationDto.setIsRegistered(iUserInfoRepository.existsByCid(cidNo));
                expectedList.add(expectedPopulationDto);
            });


            return ResponseEntity.ok().body(expectedList);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Data  not found."));
        }


    }

    private static List<JSONObject> toList(JSONArray array) {
        return IntStream.range(0, array.length())
                .mapToObj(array::getJSONObject)
                .collect(Collectors.toList());
    }



    @Override
    public ResponseEntity<?> getPersonDetailsByCid(String cid) throws IOException, ParseException, ApiException {
        CitizenDetailDto citizenDetailDto = new CitizenDetailDto();
        Resource resource = new ClassPathResource("/apiConfig/dcrcApi.properties");
        Properties props = PropertiesLoaderUtils.loadProperties(resource);
        String getCitizenDetails = props.getProperty("getCitizenDetails.endPointURL");
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.setConnectTimeout(10000, TimeUnit.MILLISECONDS);
        httpClient.setReadTimeout(10000, TimeUnit.MILLISECONDS);
        ApiClient apiClient = new ApiClient();
        apiClient.setHttpClient(httpClient);
        apiClient.setBasePath(getCitizenDetails);
        ApiAccessToken apiAccessToken = citizenDetailApiService.getApplicationToken();
        apiClient.setAccessToken(apiAccessToken.getAccess_token());

        DefaultApi api = new DefaultApi(apiClient);
        CitizenDetailsResponse citizenDetailsResponse = api.citizendetailsCidGet(cid);
        ParentdetailResponse parentdetailResponse = api.parentdetailsCidGet(cid);
        if (citizenDetailsResponse.getCitizenDetailsResponse().getCitizenDetail() != null && !citizenDetailsResponse.getCitizenDetailsResponse().getCitizenDetail().isEmpty()) {
            CitizendetailsObj citizendetailsObj = citizenDetailsResponse.getCitizenDetailsResponse().getCitizenDetail().get(0);
            ParentdetailObj parentdetailObj = parentdetailResponse.getParentDetailResponse().getParentDetail().get(0);
            char genderChar = citizendetailsObj.getGender().charAt(0);
            String genderName = "Male";
            if (genderChar == 'F') {
                genderName = "Female";
            } else if (genderChar == 'O') {
                genderName = "Other";
            }
            citizenDetailDto.setFullName(citizendetailsObj.getFirstName() + " " + citizendetailsObj.getMiddleName() + " " + citizendetailsObj.getLastName());
            citizenDetailDto.setFullName(citizenDetailDto.getFullName().replaceAll("null", ""));
            citizenDetailDto.setCid(citizendetailsObj.getCid());
            citizenDetailDto.setDob(citizendetailsObj.getDob());
            citizenDetailDto.setGender(genderChar);
            citizenDetailDto.setGenderName(genderName);
            citizenDetailDto.setFatherName(citizendetailsObj.getFatherName());
            citizenDetailDto.setFatherCid(parentdetailObj.getFatherCID());
            citizenDetailDto.setMotherName(citizendetailsObj.getMotherName());
            citizenDetailDto.setMotherCid(parentdetailObj.getMotherCID());
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
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.setConnectTimeout(10000, TimeUnit.MILLISECONDS);
        httpClient.setReadTimeout(10000, TimeUnit.MILLISECONDS);
        ApiClient apiClient = new ApiClient();
        apiClient.setHttpClient(httpClient);
        apiClient.setBasePath(getCitizenDetails);
        ApiAccessToken apiAccessToken = citizenDetailApiService.getApplicationToken();
        apiClient.setAccessToken(apiAccessToken.getAccess_token());

        DefaultApi api = new DefaultApi(apiClient);
        CitizenDetailsResponse citizenDetailsResponse = api.citizendetailsCidGet(cid);
        ParentdetailResponse parentdetailResponse = api.parentdetailsCidGet(cid);
        if (citizenDetailsResponse.getCitizenDetailsResponse().getCitizenDetail() != null && !citizenDetailsResponse.getCitizenDetailsResponse().getCitizenDetail().isEmpty()) {
            CitizendetailsObj citizendetailsObj = citizenDetailsResponse.getCitizenDetailsResponse().getCitizenDetail().get(0);
            ParentdetailObj parentdetailObj = parentdetailResponse.getParentDetailResponse().getParentDetail().get(0);
            char genderChar = citizendetailsObj.getGender().charAt(0);
            String genderName = "Male";
            if (genderChar == 'F') {
                genderName = "Female";
            } else if (genderChar == 'O') {
                genderName = "Other";
            }
            String censusDob = citizendetailsObj.getDob();
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
            citizenDetailDto.setFatherCid(parentdetailObj.getFatherCID());
            citizenDetailDto.setMotherName(citizendetailsObj.getMotherName());
            citizenDetailDto.setMotherCid(parentdetailObj.getMotherCID());
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
