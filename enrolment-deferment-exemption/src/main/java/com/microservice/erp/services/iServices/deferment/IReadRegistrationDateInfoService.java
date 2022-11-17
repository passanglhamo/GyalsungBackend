package com.microservice.erp.services.iServices.deferment;

import com.microservice.erp.domain.entities.RegistrationDateInfo;

import java.util.List;

public interface IReadRegistrationDateInfoService {
    List<RegistrationDateInfo> getAllRegistrationDateList();
}
