package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.MessageResponse;
import com.microservice.erp.domain.dto.UserDto;
import com.microservice.erp.services.iServices.ISaUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SaUserService implements ISaUserService {
//    private final ISaUserTestRepository iSaUserTestRepository;
//    private final ISaRoleRepository iSaRoleRepository;
//
//    public SaUserService(ISaUserTestRepository iSaUserTestRepository, ISaRoleRepository iSaRoleRepository) {
//        this.iSaUserTestRepository = iSaUserTestRepository;
//        this.iSaRoleRepository = iSaRoleRepository;
//    }

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
