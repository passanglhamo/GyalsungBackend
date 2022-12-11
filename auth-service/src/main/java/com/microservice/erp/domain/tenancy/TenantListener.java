package com.microservice.erp.domain.tenancy;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class TenantListener {
    @PreUpdate
    @PreRemove
    @PrePersist
    public void setTenant(TenantAware entity) {
        final Object tenantId = TenantContext.getCurrent();
        entity.setTenantId(tenantId);
    }
}
