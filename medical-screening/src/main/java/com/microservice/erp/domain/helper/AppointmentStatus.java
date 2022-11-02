package com.microservice.erp.domain.helper;

import javax.validation.constraints.NotNull;

public enum AppointmentStatus {
    Available('A'),
    Booked('I');

    private final char value;

    private AppointmentStatus(char value) {
        this.value = value;
    }

    public @NotNull Character value() {
        return value;
    }
}
