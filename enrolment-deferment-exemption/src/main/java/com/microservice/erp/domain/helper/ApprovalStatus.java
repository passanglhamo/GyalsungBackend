package com.microservice.erp.domain.helper;

import javax.validation.constraints.NotNull;

public enum ApprovalStatus {
    APPROVED('A'),
    REJECTED('R'),
    PENDING('P'),
    CANCELED('C');

    private final char value;

    private ApprovalStatus(char value) {
        this.value = value;
    }

    public @NotNull Character value() {
        return value;
    }
}
