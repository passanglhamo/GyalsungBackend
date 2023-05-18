package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.AgeCriteria;
import org.springframework.http.ResponseEntity;

public interface IAgeCriteriaService {
    ResponseEntity<?> saveAgeCriteria(AgeCriteria ageCriteria);

    ResponseEntity<?> getAgeCriteria();

}
