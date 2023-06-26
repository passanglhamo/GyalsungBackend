package com.microservice.erp.domain.mapper;

import com.microservice.erp.domain.dto.NSRegistrationDto;
import com.microservice.erp.domain.entities.NSRegistration;
import com.microservice.erp.domain.helper.ApprovalStatus;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class NSRegistrationMapper {
    public NSRegistration mapToEntity(NSRegistrationDto nsRegistrationDto) {
        NSRegistration nsRegistration = new ModelMapper().map(nsRegistrationDto, NSRegistration.class);
        nsRegistration.setRegistrationOn(new Date());
        nsRegistration.setStatus(ApprovalStatus.PENDING.value());
        return nsRegistration;
    }

    public NSRegistrationDto mapToDomain(NSRegistration nsRegistration) {
        return NSRegistrationDto.withId(
                nsRegistration.getId(),
                nsRegistration.getUserId(),
                nsRegistration.getGender(),
                nsRegistration.getYear(),
                nsRegistration.getRegistrationOn(),
                null,
                null
        );
    }
}
