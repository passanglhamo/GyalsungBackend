package com.microservice.erp.domain.helper;

import javax.validation.constraints.NotNull;

public enum ApprovalStatus {
    APPROVED('A'),
    REJECTED('R'),
    PENDING('P'),
    PENDING_APPROVAL('N'),
    PENDING_REJECTION('S'),
    REVIEWED('T'),
    REVERTED('V');

    private final char value;

    private ApprovalStatus(char value) {
        this.value = value;
    }

    public @NotNull Character value() {
        return value;
    }
}
