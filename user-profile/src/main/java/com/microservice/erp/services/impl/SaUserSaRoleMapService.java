package com.microservice.erp.services.impl;

import com.microservice.erp.domain.entities.SaUserSaRoleKey;
import com.microservice.erp.domain.repositories.ISaUserSaRoleRepository;
import com.microservice.erp.services.iServices.ISaUserSaRoleMapService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.math.BigInteger;

@Service
@AllArgsConstructor
public class SaUserSaRoleMapService implements ISaUserSaRoleMapService {
    private final ISaUserSaRoleRepository repository;
    @Override
    public ResponseEntity<?> addSaUserSaRole(@Valid AddSaUserSaRoleCommand command) {
        SaUserSaRoleKey saUserSaRoleKey= new SaUserSaRoleKey();
        saUserSaRoleKey.setUserId(command.getUserId());
        saUserSaRoleKey.setRoleId(new BigInteger("2"));
        repository.existsById(saUserSaRoleKey);

        return null;

    }
}
