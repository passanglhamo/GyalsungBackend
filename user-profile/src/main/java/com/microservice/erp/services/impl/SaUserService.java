package com.microservice.erp.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dao.UserDao;
import com.microservice.erp.domain.dto.*;
import com.microservice.erp.domain.entities.ApiAccessToken;
import com.microservice.erp.domain.entities.UserInfo;
import com.microservice.erp.domain.helper.Status;
import com.microservice.erp.domain.repositories.IUserInfoRepository;
import com.microservice.erp.services.iServices.ISaUserService;
import com.microservice.erp.services.iServices.ISignupService;
import com.squareup.okhttp.OkHttpClient;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class SaUserService implements ISaUserService {
    private final CitizenDetailApiService citizenDetailApiService;
    private final IUserInfoRepository iUserInfoRepository;
    private final ISignupService iSignupService;
    private final UserDao userDao;
    private final AddToQueue addToQueue;
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvxyz0123456789";

    @Autowired
    @Qualifier("authTemplate")
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
    public ResponseEntity<?> saveUser(UserDto userDto) throws IOException, ParseException, ApiException {
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

            String url = properties.getAuthServiceToGetUserById() + item.getUserId();
            ResponseEntity<AuthUserDto> response = restTemplate.exchange(url, HttpMethod.GET, request, AuthUserDto.class);
            userProfileDto.setUserId(item.getUserId());
            userProfileDto.setFullName(item.getFullName());
            userProfileDto.setCid(item.getCid());
            userProfileDto.setMobileNo(item.getMobileNo());
            userProfileDto.setEmail(item.getEmail());
            userProfileDto.setGender(item.getGender());
            userProfileDto.setGenderName(item.getGender().equals('M') ? "Male" : "Female");
            userProfileDto.setDob(item.getDob());
            userProfileDto.setStatus(Objects.requireNonNull(response.getBody()).getStatus());
            userProfileDto.setStatusName(Objects.requireNonNull(response.getBody()).getStatus().equals(Status.Active.value()) ? Status.Active.name() : Status.Inactive.name());
            userProfileDto.setRoles(Objects.requireNonNull(response.getBody()).getRoles());
            userProfileDtos.add(userProfileDto);
        });
        if (saUsers.size() > 0) {
            return ResponseEntity.ok(userProfileDtos);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Data not found."));
        }
    }


    @Override
    public ResponseEntity<?> getOfficersByUserType(String authHeader, Character userTypeVal) {

        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<String> request = new HttpEntity<>(headers);
        List<UserInfo> saUsers = iUserInfoRepository.findAllBySignupUserOrderByFullNameAsc('N');

        List<UserProfileDto> userProfileDtos = new ArrayList<>();
        saUsers.forEach(item -> {
            UserProfileDto userProfileDto = new UserProfileDto();
            String url = properties.getAuthServiceToGetUserById() + item.getUserId();
            ResponseEntity<AuthUserDto> response = restTemplate.exchange(url, HttpMethod.GET, request, AuthUserDto.class);
            String userType = new JSONArray("[" + Objects.requireNonNull(response.getBody()).getRoles().toString().substring(1, Objects.requireNonNull(response.getBody()).getRoles().toString().length() - 1).replaceAll("=", ":") + "]")
                    .getJSONObject(0).getString("userType");
            if (userType.equals(userTypeVal.toString())) {
                userProfileDto.setUserId(item.getUserId());
                userProfileDto.setFullName(item.getFullName());
                userProfileDto.setCid(item.getCid());
                userProfileDto.setMobileNo(item.getMobileNo());
                userProfileDto.setEmail(item.getEmail());
                userProfileDto.setGender(item.getGender());
                userProfileDto.setStatus(Objects.requireNonNull(response.getBody()).getStatus());
                userProfileDto.setRoles(Objects.requireNonNull(response.getBody()).getRoles());
                userProfileDtos.add(userProfileDto);
            }

        });

        return ResponseEntity.ok(userProfileDtos);

    }

    private ResponseEntity<?> addNewUser(UserDto userDto) throws IOException, ParseException, ApiException {

        if (!userDto.getCid().isEmpty()) {
            Optional<UserInfo> saUserCid = iUserInfoRepository.findByCid(userDto.getCid());
            if (saUserCid.isPresent()) {
                return ResponseEntity.badRequest().body(new MessageResponse("CID " + userDto.getCid() + " already in use."));
            }
        }

        UserInfo saUser = new ModelMapper().map(userDto, UserInfo.class);
        saUser.setUsername(userDto.getCid());


        saUser.setSignupUser('N');
        List<BigInteger> saRoleDtos = userDto.getRoles();
        if (saRoleDtos.size() == 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("Roles not selected."));
        }

        ResponseEntity<CitizenDetailDto> validateCitizenDetails = (ResponseEntity<CitizenDetailDto>) iSignupService.validateCitizenDetails(userDto.getCid(), userDto.getDateOfBirth());
        Date birthDate = new SimpleDateFormat("dd/MM/yyyy").parse(validateCitizenDetails.getBody().getDob());

        String birthDateString = validateCitizenDetails.getBody().getDob();
        saUser.setDob(birthDate);

        saUser.setSignupUser('N');
        UserInfo userInfoDb = iUserInfoRepository.findFirstByOrderByUserIdDesc();
        BigInteger userIdNew = userInfoDb == null ? BigInteger.ONE : userInfoDb.getUserId().add(BigInteger.ONE);
        saUser.setUserId(userIdNew);

        BigInteger userId = iUserInfoRepository.save(saUser).getUserId();

        //todo: need to pass dob in string format
        EventBusUser eventBusUser = EventBusUser.withId(userId, userDto.getStatus(), saUser.getCid(), birthDateString, saUser.getEmail(), saUser.getMobileNo()
                , saUser.getUsername(), userDto.getPassword(), saUser.getSignupUser(), userDto.getRoles());
        addToQueue.addToUserQueue("addUser", eventBusUser);

        return ResponseEntity.ok(new MessageResponse("User added successfully!"));
    }

    private ResponseEntity<?> editUser(UserDto userDto) throws JsonProcessingException {
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
        List<BigInteger> saRoleDtos = userDto.getRoles();
        if (saRoleDtos.size() != 0) {
            EventBusUser eventBusUser = EventBusUser.withId(saUserDb.getUserId(), userDto.getStatus(), saUser.getCid(), null, saUserDb.getEmail(), saUserDb.getMobileNo()
                    , saUserDb.getUsername(), null, saUserDb.getSignupUser(), userDto.getRoles());
            addToQueue.addToUserQueue("addUser", eventBusUser);
        }

        return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
    }


}
