package com.microservice.erp.domain.mapper;

import com.microservice.erp.domain.dto.RoleDto;
import com.microservice.erp.domain.entities.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public RoleDto mapToDomain(Role saRole) {
        return RoleDto.withId(
                saRole.getId(),
                saRole.getRoleName(),
                saRole.getUserType()
        );
    }
}

