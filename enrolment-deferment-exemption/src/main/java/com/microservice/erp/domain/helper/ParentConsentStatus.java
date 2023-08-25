package com.microservice.erp.domain.helper;

public enum ParentConsentStatus {

    SUPPORTED('A', "Supported"),
    NOT_SUPPORTED('R', "Not Supported");

    private final char value;
    private final String name;

    ParentConsentStatus(char value, String name) {
        this.value = value;
        this.name = name;
    }

    public char value() { // Removed @NotNull annotation for simplicity
        return value;
    }

    public String getName() { // Getter for the name
        return name;
    }

    public static ParentConsentStatus fromValue(char value) {
        for (ParentConsentStatus status : ParentConsentStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid ApprovalStatus value: " + value);
    }
}
