package com.microservice.erp.domain.tenancy;

public interface TenantAware<ID> {
    ID getTenantId();
    void setTenantId(ID tenantId);
}
