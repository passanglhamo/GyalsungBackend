package com.microservice.erp.services.definition;

import com.infoworks.lab.rest.models.Response;

public interface iResetPassword {
    Response didForget(String username);

    Response doReset(String token, String oldPass, String newPass);
}
