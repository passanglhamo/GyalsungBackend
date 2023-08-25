package com.microservice.erp.domain.mapper;

import com.microservice.erp.domain.dto.EarlyEnlistmentDto;
import com.microservice.erp.domain.entities.EarlyEnlistment;
import org.springframework.stereotype.Component;

@Component
public class EarlyEnlistmentMapper {
    public EarlyEnlistmentDto mapToDomain(EarlyEnlistment earlyEnlistment) {
        return EarlyEnlistmentDto.withId(
                earlyEnlistment.getEnlistmentId(),
                earlyEnlistment.getUserId(),
                earlyEnlistment.getStatus(),
                earlyEnlistment.getEnlistmentYear(),
                earlyEnlistment.getApplicationDate(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

}
