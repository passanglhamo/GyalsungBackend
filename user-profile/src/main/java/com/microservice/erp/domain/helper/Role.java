package com.microservice.erp.domain.helper;

import javax.validation.constraints.NotNull;

public enum Role {
    STUDENT('S'),
    MEDICAL_DEFERMENT_OFFICER('M'),
    NON_MEDICAL_DEFERMENT_OFFICER('N');

    private final char value;

    private Role(char value) {
        this.value = value;
    }

    public @NotNull Character value() {
        return value;
    }
}
