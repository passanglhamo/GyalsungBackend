package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dao.UserDao;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class SaUserService implements ISaUserService {
    private final ISaUserRepository iSaUserRepository;
    private final ISaRoleRepository iSaRoleRepository;
    private final UserDao userDao;
//    private final BCryptPasswordEncoder passwordEncoder;

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
    public ResponseEntity<?> saveUser(UserDto userDto) {
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

    private ResponseEntity<?> addNewUser(UserDto userDto) {
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
        //todo:send email and sms
        return ResponseEntity.ok(new MessageResponse("User added successfully!"));
    }

    private ResponseEntity<?> editUser(UserDto userDto) {
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
        //todo:send email and sms
        return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
    }
}
