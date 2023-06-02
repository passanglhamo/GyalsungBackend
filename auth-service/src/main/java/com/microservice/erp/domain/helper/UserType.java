package com.microservice.erp.domain.helper;

import javax.validation.constraints.NotNull;

public enum UserType {
    STUDENT('S'),
    OPERATOR('R');

    private final char value;

    private UserType(char value) {
        this.value = value;
    }

    public @NotNull Character value() {
        return value;
    }
}
