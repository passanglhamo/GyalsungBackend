package com.microservice.erp.domain.helper;

import javax.validation.constraints.NotNull;

public enum RoleStatus {
    STUDENT('S'),
    MEDICAL_DEFERMENT_OFFICER('M'),
    NON_MEDICAL_DEFERMENT_OFFICER('N'),
    SENIOR_DEFERMENT_OFFICER('H');

    private final char value;

    private RoleStatus(char value) {
        this.value = value;
    }

    public @NotNull Character value() {
        return value;
    }
}
