package com.microservice.erp.services.definition;

import com.microservice.erp.domain.models.NewAccountRequest;
import com.microservice.erp.domain.models.NewTenantRequest;
import com.infoworks.lab.rest.models.Response;

public interface iRegistration {

    Response createNew(NewAccountRequest account);

    Response isAccountExist(String username);

    Response createNewTenant(NewTenantRequest tenant);
}
