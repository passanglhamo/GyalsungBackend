package com.microservice.erp.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dao.UserDao;
import com.microservice.erp.domain.dto.*;
import com.microservice.erp.domain.entities.ApiAccessToken;
import com.microservice.erp.domain.entities.UserInfo;
import com.microservice.erp.domain.repositories.IUserInfoRepository;
import com.microservice.erp.services.iServices.ISaUserService;
import com.squareup.okhttp.OkHttpClient;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.wso2.client.api.ApiClient;
import org.wso2.client.api.ApiException;
import org.wso2.client.api.DCRC_CitizenDetailsAPI.DefaultApi;
import org.wso2.client.model.DCRC_CitizenDetailsAPI.CitizenDetailsResponse;
import org.wso2.client.model.DCRC_CitizenDetailsAPI.CitizendetailsObj;

import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class SaUserService implements ISaUserService {
    private final CitizenDetailApiService citizenDetailApiService;
    private final IUserInfoRepository iUserInfoRepository;
    private final UserDao userDao;
    private final AddToQueue addToQueue;
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvxyz0123456789";

    @Autowired @Qualifier("authTemplate")
    RestTemplate restTemplate;

    @Override
    public ResponseEntity<?> getCensusDetailByCid(String cid) throws IOException, ParseException, ApiException {
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

        //region off this in stagging
        ApiAccessToken apiAccessToken = citizenDetailApiService.getApplicationToken();
        apiClient.setAccessToken(apiAccessToken.getAccess_token());
        //endregion

        DefaultApi api = new DefaultApi(apiClient);
        CitizenDetailsResponse citizenDetailsResponse = api.citizendetailsCidGet(cid);
        if (citizenDetailsResponse.getCitizenDetailsResponse().getCitizenDetail() != null && !citizenDetailsResponse.getCitizenDetailsResponse().getCitizenDetail().isEmpty()) {
            CitizendetailsObj citizendetailsObj = citizenDetailsResponse.getCitizenDetailsResponse().getCitizenDetail().get(0);

            char genderChar = citizendetailsObj.getGender().charAt(0);

            //todo need to change static code
            String genderName = "Male";
            if (genderChar == 'F') {
                genderName = "Female";
            } else if (genderChar == 'O') {
                genderName = "Other";
            }

            String censusDob = citizendetailsObj.getDob();
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


    @Override
    public ResponseEntity<?> saveUser(UserDto userDto) throws JsonProcessingException {
        ResponseEntity<?> responseEntity;
        if (userDto.getUserId() == null) {
            responseEntity = addNewUser(userDto);
        } else {
            responseEntity = editUser(userDto);
        }
        return responseEntity;
    }

    @Override
    public ResponseEntity<?> getUsers(String authHeader) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);

        //todo remove static code
        List<UserInfo> saUsers = iUserInfoRepository.findAllBySignupUserOrderByFullNameAsc('N');
        List<UserProfileDto> userProfileDtos = new ArrayList<>();
        saUsers.forEach(item -> {
            UserProfileDto userProfileDto = new UserProfileDto();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", authHeader);
            HttpEntity<String> request = new HttpEntity<>(headers);

            String url = properties.getAuthServiceToGetUserById() + item.getId();
            ResponseEntity<AuthUserDto> response = restTemplate.exchange(url, HttpMethod.GET, request, AuthUserDto.class);
            userProfileDto.setUserId(item.getId());
            userProfileDto.setFullName(item.getFullName());
            userProfileDto.setCid(item.getCid());
            userProfileDto.setMobileNo(item.getMobileNo());
            userProfileDto.setEmail(item.getEmail());
            userProfileDto.setGender(item.getGender());
            userProfileDto.setStatus(Objects.requireNonNull(response.getBody()).getStatus());
            userProfileDto.setRoles(Objects.requireNonNull(response.getBody()).getRoles());
            userProfileDtos.add(userProfileDto);
        });
        if (saUsers.size() > 0) {
            return ResponseEntity.ok(userProfileDtos);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Data not found."));
        }
    }

    private ResponseEntity<?> addNewUser(UserDto userDto) throws JsonProcessingException {
        Optional<UserInfo> saUserEmail = iUserInfoRepository.findByEmail(userDto.getEmail());
        if (saUserEmail.isPresent()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email already in use."));
        }

        if(!userDto.getCid().isEmpty()){
            Optional<UserInfo> saUserCid = iUserInfoRepository.findByCid(userDto.getCid());
            if (saUserCid.isPresent()) {
                return ResponseEntity.badRequest().body(new MessageResponse("CID " + userDto.getCid() + " already in use."));
            }
        }

        UserInfo saUser = new ModelMapper().map(userDto, UserInfo.class);
        saUser.setUsername(userDto.getEmail());

        //todo remove static code
        saUser.setSignupUser('N');
        String password = generatePassword(8); //to generate password and send email
        List<BigInteger> saRoleDtos = userDto.getRoles();
        if (saRoleDtos.size() == 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("Roles not selected."));
        }

//todo remove static code
        saUser.setSignupUser('N');
        BigInteger userId = iUserInfoRepository.save(saUser).getId();
        EventBusUser eventBusUser = EventBusUser.withId(userId, userDto.getStatus(), saUser.getCid(), saUser.getEmail()
                , saUser.getUsername(), password, saUser.getSignupUser(), userDto.getRoles());
        addToQueue.addToUserQueue("addUser", eventBusUser);

        String emailBody = "Dear " + userDto.getFullName() + ", " + "Your information has been added to Gyalsung MIS against this your email. " + "Please login in using email: " + userDto.getEmail() + " and password " + password;
        String subject = "User Added to Gyalsung System";
        EventBus eventBusEmail = EventBus.withId(userDto.getEmail(), null, null, emailBody, subject, null);
        String smsBody = "Dear " + userDto.getFullName() + ", " + " Your information has been added to Gyalsung MIS against this your email. " + "Please check your email " + userDto.getEmail() + " to see login credentials.";
        EventBus eventBusSms = EventBus.withId(null, null, null, smsBody, null, userDto.getMobileNo());

        //todo:need to get topic name from properties file
        addToQueue.addToQueue("email", eventBusEmail);
        addToQueue.addToQueue("sms", eventBusSms);
        return ResponseEntity.ok(new MessageResponse("User added successfully!"));
    }

    private ResponseEntity<?> editUser(UserDto userDto) throws JsonProcessingException {
        String isEmailAlreadyInUse = userDao.isEmailAlreadyInUse(userDto.getEmail(), userDto.getUserId());
        if (isEmailAlreadyInUse != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email already in use."));
        }
        String isCidAlreadyInUse = userDao.isCidAlreadyInUse(userDto.getCid(), userDto.getUserId());
        if (isCidAlreadyInUse != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("CID " + userDto.getCid() + " already in use."));
        }

        UserInfo saUserDb = iUserInfoRepository.findById(userDto.getUserId()).get();
        UserInfo saUser = new ModelMapper().map(saUserDb, UserInfo.class);
        saUser.setFullName(userDto.getFullName());
        saUser.setGender(userDto.getGender());
        saUser.setMobileNo(userDto.getMobileNo());
        saUser.setEmail(userDto.getEmail());
        //saUser.setStatus(userDto.getStatus());
        List<BigInteger> saRoleDtos = userDto.getRoles();
        if (saRoleDtos.size() == 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("Roles not selected."));
        }
        BigInteger userId = iUserInfoRepository.save(saUser).getId();
        EventBusUser eventBusUser = EventBusUser.withId(userId, userDto.getStatus(), saUser.getCid(), saUser.getEmail()
                , saUser.getUsername(), null, saUser.getSignupUser(), userDto.getRoles());
        addToQueue.addToUserQueue("addUser", eventBusUser);

        String emailBody = "Dear " + saUser.getFullName() + ", " + "Your information in Gyalsung MIS has been updated. " + "Please login in using email: " + saUser.getEmail();
        String subject = "User Updated in Gyalsung System";
        EventBus eventBusEmail = EventBus.withId(userDto.getEmail(), null, null, emailBody, subject, userDto.getMobileNo());

        String smsBody = "Dear " + saUser.getFullName() + ", " + " Your information in Gyalsung MIS has been updated. " + "Please check your email " + saUser.getEmail() + " to see login credentials.";
        EventBus eventBusSms = EventBus.withId(null, null, null, smsBody, null, userDto.getMobileNo());

        //todo:need to get topic name from properties file
        addToQueue.addToQueue("email", eventBusEmail);
        addToQueue.addToQueue("sms", eventBusSms);
        return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
    }

    public static String generatePassword(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
}
