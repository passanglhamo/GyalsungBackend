package com.microservice.erp.services.impl;

import com.microservice.erp.domain.entities.SaRole;
import com.microservice.erp.domain.repositories.ISaRoleRepository;
import com.microservice.erp.services.iServices.ISaRoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SaRoleService implements ISaRoleService {

    private final ISaRoleRepository repository;

    @Override
    public SaRole saveRole(SaRole role) {
        return repository.save(role);
    }
}
