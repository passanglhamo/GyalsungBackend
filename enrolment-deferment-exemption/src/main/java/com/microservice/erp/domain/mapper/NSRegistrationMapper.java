package com.microservice.erp.domain.mapper;

import com.microservice.erp.domain.dto.NSRegistrationDto;
import com.microservice.erp.domain.entities.NSRegistration;
import com.microservice.erp.domain.helper.ApprovalStatus;
import com.microservice.erp.domain.repositories.INSRegistrationRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Date;

@Component
@AllArgsConstructor
public class NSRegistrationMapper {
    private final INSRegistrationRepository repository;

    public NSRegistration mapToEntity(NSRegistrationDto nsRegistrationDto) {
        NSRegistration nsRegistration = new ModelMapper().map(nsRegistrationDto, NSRegistration.class);
        NSRegistration nsRegistrationDB = repository.findFirstByOrderByRegistrationIdDesc();
        BigInteger registeredId = nsRegistrationDB == null ? BigInteger.ONE : nsRegistrationDB.getRegistrationId().add(BigInteger.ONE);
        nsRegistration.setRegistrationId(registeredId);
        nsRegistration.setRegistrationOn(new Date());
        nsRegistration.setStatus(ApprovalStatus.PENDING.value());
        return nsRegistration;
    }

    public NSRegistrationDto mapToDomain(NSRegistration nsRegistration) {
        return NSRegistrationDto.withId(
                nsRegistration.getRegistrationId(),
                nsRegistration.getUserId(),
                nsRegistration.getGender(),
                nsRegistration.getYear(),
                nsRegistration.getRegistrationOn(),
                null,
                null
        );
    }
}
