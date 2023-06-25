package com.microservice.erp.domain.mapper;

import com.microservice.erp.domain.dto.NSRegistrationDto;
import com.microservice.erp.domain.entities.NSRegistration;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class NSRegistrationMapper {
    public NSRegistration mapToEntity(NSRegistrationDto nsRegistrationDto) {
        NSRegistration nsRegistration = new ModelMapper().map(nsRegistrationDto, NSRegistration.class);
        nsRegistration.setRegistrationOn(new Date());
        return nsRegistration;
    }
}
