package com.microservice.erp.domain.helper;

public enum MailSentStatus {
    SENT('S', "Sent"),
    NOT_SENT('N', "Not Sent");

    private final char value;
    private final String name;

    MailSentStatus(char value, String name) {
        this.value = value;
        this.name = name;
    }

    public char value() { // Removed @NotNull annotation for simplicity
        return value;
    }

    public String getName() { // Getter for the name
        return name;
    }

    public static MailSentStatus fromValue(char value) {
        for (MailSentStatus status : MailSentStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid ApprovalStatus value: " + value);
    }
}
