package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.MessageResponse;
import com.microservice.erp.domain.dto.UserDto;
import com.microservice.erp.domain.entities.SaRole;
import com.microservice.erp.domain.repositories.ISaRoleRepository;
import com.microservice.erp.services.iServices.ISaUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaUserService implements ISaUserService {
    //    private final ISaUserTestRepository iSaUserTestRepository;
    private final ISaRoleRepository iSaRoleRepository;

    public SaUserService(ISaRoleRepository iSaRoleRepository) {
        this.iSaRoleRepository = iSaRoleRepository;
    }


    @Override
    public ResponseEntity<?> getAllRoles() {
        List<SaRole> saRoleList = iSaRoleRepository.findAllByOrderByRoleNameAsc();
        if (saRoleList.size() > 0) {
            return ResponseEntity.ok(saRoleList);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Roles not found"));
        }
    }

    @Override
    public ResponseEntity<?> addUser(UserDto userDto) {

        /*SaUserTest saUserTest = new ModelMapper().map(userDto, SaUserTest.class);

        List<SaRoleDto> saRoleDtos = userDto.getSaRoleDtos();
        Set<SaRole> saRoles = new HashSet<>();

        saRoleDtos.forEach(saRoleDto -> {
            SaRole saRoleDb = iSaRoleRepository.findByRoleId(saRoleDto.getRoleId());
            //todo: if saRoleDb is null return error message, break the loop
            saRoles.add(saRoleDb);
        });

        saUserTest.setSaRoles(saRoles);
        iSaUserTestRepository.save(saUserTest);*/

        return ResponseEntity.ok(new MessageResponse("User added successfully!"));
    }
}
