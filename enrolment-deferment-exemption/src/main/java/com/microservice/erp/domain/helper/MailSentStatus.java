package com.microservice.erp.domain.helper;

import javax.validation.constraints.NotNull;

public enum MailSentStatus {
    SENT('S'),
    NOT_SENT('N');
    private final char value;

    private MailSentStatus(char value) {
        this.value = value;
    }

    public @NotNull Character value() {
        return value;
    }
}
