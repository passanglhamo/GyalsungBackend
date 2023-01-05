package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.MessageResponse;
import com.microservice.erp.domain.dto.RoleDto;
import com.microservice.erp.domain.entities.Role;
import com.microservice.erp.domain.mapper.RoleMapper;
import com.microservice.erp.domain.repositories.IRoleRepository;
import com.microservice.erp.services.definition.IRoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SaRoleService implements IRoleService {

    private final IRoleRepository repository;
    private final RoleMapper mapper;

    @Override
    public ResponseEntity<?> saveRole(Role role) {
        if (role.getIsOpenUser().equals('Y')) {
            if (repository.existsByIsOpenUser('Y')) {
                return new ResponseEntity<>("There can be only one student role.", HttpStatus.ALREADY_REPORTED);
            }
        }
        repository.save(role);
        return ResponseEntity.ok("Data saved successfully.");
    }

    @Override
    public List<RoleDto> getAllRoleList() {

        return repository.findAllByOrderByRoleNameAsc()
                .stream()
                .map(mapper::mapToDomain)
                .collect(Collectors.toList());

    }

    @Override
    public ResponseEntity<?> updateRole(RoleDto role) {


        if(role.getIsOpenUser().equals('Y')){
            if (repository.existsByIsOpenUserAndIdNot('Y', role.getId())) {
                return new ResponseEntity<>("There can be only one student role.", HttpStatus.ALREADY_REPORTED);
            }
        }

        repository.findById(role.getId()).ifPresent(d -> {
            d.setRoleName(role.getRoleName());
            d.setIsOpenUser(role.getIsOpenUser());
            repository.save(d);

        });
        return ResponseEntity.ok("Data updated successfully.");
    }
}

