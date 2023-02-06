package com.microservice.erp.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dao.UserDao;
import com.microservice.erp.domain.dto.*;
import com.microservice.erp.domain.entities.UserInfo;
import com.microservice.erp.domain.repositories.IUserInfoRepository;
import com.microservice.erp.services.iServices.ISaUserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SaUserService implements ISaUserService  {
    private final IUserInfoRepository iUserInfoRepository;
    private final UserDao userDao;
    private final AddToQueue addToQueue;
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvxyz0123456789";

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

        List<UserInfo> saUsers = iUserInfoRepository.findAllBySignupUserOrderByFullNameAsc('N');
        List<UserProfileDto> userProfileDtos = new ArrayList<>();
        saUsers.forEach(item -> {
            UserProfileDto userProfileDto = new UserProfileDto();
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", authHeader);
            HttpEntity<String> request = new HttpEntity<>(headers);
            //String urlSd = properties.getAuthServiceURL();
            //String url = "http://localhost:80/AUTH-SERVICE/api/auth/auth/v1/userByUserId?userId=" + item.getId();
            String url = "http://localhost:8083/api/auth/auth/v1/userByUserId?userId=" + item.getId();
            ResponseEntity<AuthUserDto> response = restTemplate.exchange(url, HttpMethod.GET, request, AuthUserDto.class);
            userProfileDto.setUserId(item.getId());
            userProfileDto.setFullName(item.getFullName());
            userProfileDto.setMobileNo(item.getMobileNo());
            userProfileDto.setEmail(item.getEmail());
            userProfileDto.setGender(item.getGender());
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
        UserInfo saUser = new ModelMapper().map(userDto, UserInfo.class);
        saUser.setUsername(userDto.getEmail());
        saUser.setSignupUser('N');
        String password = generatePassword(8); //to generate password and send email
        List<BigInteger> saRoleDtos = userDto.getRoles();
        if (saRoleDtos.size() == 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("Roles not selected."));
        }

        saUser.setSignupUser('N');
        BigInteger userId = iUserInfoRepository.save(saUser).getId();
        EventBusUser eventBusUser = EventBusUser.withId(userId, saUser.getCid(), saUser.getEmail()
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
        EventBusUser eventBusUser = EventBusUser.withId(userId, saUser.getCid(), saUser.getEmail()
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
