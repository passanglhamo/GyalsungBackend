package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.PersonStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IPersonStatusService {
    List<PersonStatus> getPersonStatus();
}
