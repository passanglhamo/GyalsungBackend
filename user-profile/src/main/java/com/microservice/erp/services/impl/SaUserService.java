package com.microservice.erp.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dao.UserDao;
import com.microservice.erp.domain.dto.EventBus;
import com.microservice.erp.domain.dto.MessageResponse;
import com.microservice.erp.domain.dto.SaRoleDto;
import com.microservice.erp.domain.dto.UserDto;
import com.microservice.erp.domain.entities.SaRole;
import com.microservice.erp.domain.entities.SaUser;
import com.microservice.erp.domain.repositories.ISaRoleRepository;
import com.microservice.erp.domain.repositories.ISaUserRepository;
import com.microservice.erp.services.iServices.ISaUserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class SaUserService implements ISaUserService {
    private final ISaUserRepository iSaUserRepository;
    private final ISaRoleRepository iSaRoleRepository;
    private final UserDao userDao;
    private final AddToQueue addToQueue;
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvxyz0123456789";

    private final BCryptPasswordEncoder passwordEncoder;

//    @Value("${topic.email}")
//    private String emailTopic;
//
//    @Value("${topic.sms}")
//    private String smsTopic;

    @Override
    public ResponseEntity<?> getAllRoles() {
        List<SaRole> saRoleList = iSaRoleRepository.findAllByOrderByRoleNameAsc();
        if (saRoleList.size() > 0) {
            return ResponseEntity.ok(saRoleList);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Roles not found."));
        }
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
    public ResponseEntity<?> getUsers() {
        List<SaUser> saUsers = iSaUserRepository.findAllBySignupUserOrderByFullNameAsc('N');
        if (saUsers.size() > 0) {
            return ResponseEntity.ok(saUsers);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Data not found."));
        }
    }

    private ResponseEntity<?> addNewUser(UserDto userDto) throws JsonProcessingException {
        SaUser saUserEmail = iSaUserRepository.findByEmail(userDto.getEmail());
        if (saUserEmail != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email already in use."));
        }
        SaUser saUser = new ModelMapper().map(userDto, SaUser.class);
        saUser.setUsername(userDto.getEmail());
        saUser.setStatus('A');
        saUser.setSignupUser('N');
        String password = generatePassword(8); //to generate password and send email
        saUser.setPassword(passwordEncoder.encode(password));
        List<BigInteger> saRoleDtos = userDto.getRoles();
        if (saRoleDtos.size() == 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("Roles not selected."));
        }
        Set<SaRole> saRoles = new HashSet<>();
        saRoleDtos.forEach(roleId -> {
            SaRole saRoleDb = iSaRoleRepository.findById(roleId).get();
            saRoles.add(saRoleDb);
        });
        saUser.setRoles(saRoles);
        saUser.setSecrets(SaUser.createRandomMapOfSecret());
        iSaUserRepository.save(saUser);
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
        SaUser saUserDb = iSaUserRepository.findById(userDto.getUserId()).get();
        SaUser saUser = new ModelMapper().map(saUserDb, SaUser.class);
        saUser.setFullName(userDto.getFullName());
        saUser.setGender(userDto.getGender());
        saUser.setMobileNo(userDto.getMobileNo());
        saUser.setEmail(userDto.getEmail());
        saUser.setStatus(userDto.getStatus());
        List<BigInteger> saRoleDtos = userDto.getRoles();
        if (saRoleDtos.size() == 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("Roles not selected."));
        }

        Set<SaRole> saRoles = new HashSet<>();
        saRoleDtos.forEach(roleId -> {
            SaRole saRoleDb = iSaRoleRepository.findById(roleId).get();
            saRoles.add(saRoleDb);
        });
        saUser.setRoles(saRoles);
        iSaUserRepository.save(saUser);
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
