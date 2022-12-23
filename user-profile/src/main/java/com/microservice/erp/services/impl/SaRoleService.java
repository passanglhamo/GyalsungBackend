package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.RoleDto;
import com.microservice.erp.domain.entities.SaRole;
import com.microservice.erp.domain.mapper.RoleMapper;
import com.microservice.erp.domain.repositories.ISaRoleRepository;
import com.microservice.erp.services.iServices.ISaRoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SaRoleService implements ISaRoleService {

    private final ISaRoleRepository repository;
    private final RoleMapper mapper;

    @Override
    public SaRole saveRole(SaRole role) {
        return repository.save(role);
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
        repository.findById(role.getId()).ifPresent(d -> {
            d.setRoleName(role.getRoleName());
            d.setIsOpenUser(role.getIsOpenUser());
            repository.save(d);

        });
        return ResponseEntity.ok("Data updated successfully");
    }
}
