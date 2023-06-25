package com.microservice.erp.domain.helper;

import javax.validation.constraints.NotNull;

public enum ActiveStatus {
    Active('A'),
    Inactive('I');

    private final char value;

    private ActiveStatus(char value) {
        this.value = value;
    }

    public @NotNull Character value() {
        return value;
    }
}
