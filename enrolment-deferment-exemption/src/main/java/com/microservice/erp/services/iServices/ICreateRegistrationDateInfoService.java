package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.RegistrationDateInfo;

public interface ICreateRegistrationDateInfoService {
    RegistrationDateInfo saveRegistrationDateInfo(RegistrationDateInfo registrationDateInfo);
}
