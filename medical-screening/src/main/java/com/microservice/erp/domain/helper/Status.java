package com.microservice.erp.domain.helper;

import javax.validation.constraints.NotNull;

public enum Status {
    Active('A'),
    Inactive('I');

    private final char value;

    private Status(char value) {
        this.value = value;
    }

    public @NotNull Character value() {
        return value;
    }
}
