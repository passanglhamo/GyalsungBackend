package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.FieldSpecialization;

public interface ICreateFieldSpecializationService {

    FieldSpecialization saveFieldSpec(FieldSpecialization fieldSpecialization);
}
