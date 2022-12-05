package com.microservice.erp.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservice.erp.domain.dao.UserDao;
import com.microservice.erp.domain.dto.CoachBus;
import com.microservice.erp.domain.dto.MessageResponse;
import com.microservice.erp.domain.dto.SaRoleDto;
import com.microservice.erp.domain.dto.UserDto;
import com.microservice.erp.domain.entities.SaRole;
import com.microservice.erp.domain.entities.SaUser;
import com.microservice.erp.domain.repositories.ISaRoleRepository;
import com.microservice.erp.domain.repositories.ISaUserRepository;
import com.microservice.erp.services.iServices.ISaUserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class SaUserService implements ISaUserService {
    private final ISaUserRepository iSaUserRepository;
    private final ISaRoleRepository iSaRoleRepository;
    private final UserDao userDao;
    private final AddToQueue addToQueue;
    //    private final BCryptPasswordEncoder passwordEncoder;

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
        //todo:generate password and send email
        String password = "pw";
        saUser.setPassword(password);
        List<SaRoleDto> saRoleDtos = userDto.getRoles();
        if (saRoleDtos.size() == 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("Roles not selected."));
        }
        Set<SaRole> saRoles = new HashSet<>();
        saRoleDtos.forEach(saRoleDto -> {
            SaRole saRoleDb = iSaRoleRepository.findByRoleId(saRoleDto.getRoleId());
            if (saRoleDb == null) {
                return;
            }
            saRoles.add(saRoleDb);
        });
        saUser.setSaRoles(saRoles);
        iSaUserRepository.save(saUser);
        String emailBody = "Dear " + userDto.getFullName() + ", " + "Your information has been added to Gyalsung MIS against this your email. " + "Please login in using email: " + userDto.getEmail() + " and password " + password;
        String subject = "User Added to Gyalsung System";
        CoachBus coachBusEmail = CoachBus.withId(userDto.getEmail(), null, null, emailBody, subject, userDto.getMobileNo());

        String smsBody = "Dear " + userDto.getFullName() + ", " + " Your information has been added to Gyalsung MIS against this your email. " + "Please check your email " + userDto.getEmail() + " to see login credentials.";
        CoachBus coachBusSms = CoachBus.withId(userDto.getEmail(), null, null, smsBody, subject, userDto.getMobileNo());
//todo:need to get topic name from properties file
        addToQueue.addToQueue("email", coachBusEmail);
        addToQueue.addToQueue("sms", coachBusSms);
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
        List<SaRoleDto> saRoleDtos = userDto.getRoles();
        if (saRoleDtos.size() == 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("Roles not selected."));
        }
        Set<SaRole> saRoles = new HashSet<>();
        saRoleDtos.forEach(saRoleDto -> {
            SaRole saRoleDb = iSaRoleRepository.findByRoleId(saRoleDto.getRoleId());
            if (saRoleDb == null) {
                return;
            }
            saRoles.add(saRoleDb);
        });
        saUser.setSaRoles(saRoles);
        iSaUserRepository.save(saUser);
        String emailBody = "Dear " + saUser.getFullName() + ", " + "Your information in Gyalsung MIS has been updated. " + "Please login in using email: " + saUser.getEmail();
        String subject = "User Updated in Gyalsung System";
        CoachBus coachBusEmail = CoachBus.withId(userDto.getEmail(), null, null, emailBody, subject, userDto.getMobileNo());

        String smsBody = "Dear " + saUser.getFullName() + ", " + " Your information in Gyalsung MIS has been updated. " + "Please check your email " + saUser.getEmail() + " to see login credentials.";
        CoachBus coachBusSms = CoachBus.withId(userDto.getEmail(), null, null, smsBody, subject, userDto.getMobileNo());

        //todo:need to get topic name from properties file
        addToQueue.addToQueue("email", coachBusEmail);
        addToQueue.addToQueue("sms", coachBusSms);
        return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
    }
}
