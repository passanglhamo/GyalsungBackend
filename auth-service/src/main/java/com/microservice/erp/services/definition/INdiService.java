package com.microservice.erp.services.definition;

import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface INdiService {

    ResponseEntity<?> getProofRequest() throws IOException, InterruptedException;

}
