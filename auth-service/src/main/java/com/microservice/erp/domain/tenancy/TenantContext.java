package com.microservice.erp.domain.tenancy;

public class TenantContext {

    private static ThreadLocal<Object> currentTenant = new ThreadLocal<>();

    public static Object getCurrent() {
        return currentTenant.get();
    }

    public static void setCurrent(Object tenant) {
        currentTenant.set(tenant);
    }

    public static void clear() {
        currentTenant.set(null);
    }
}
