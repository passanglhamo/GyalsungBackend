package com.microservice.erp.domain.helper;

public enum EnlistmentStatus {
    PENDING('P',"Pending"),
    APPROVED('A',"Approved"),
    REJECTED('R',"Rejected");

    private final char value;
    private final String name;

    EnlistmentStatus(char value, String name) {
        this.value = value;
        this.name = name;
    }

    public char value() { // Removed @NotNull annotation for simplicity
        return value;
    }

    public String getName() { // Getter for the name
        return name;
    }

    public static EnlistmentStatus fromValue(char value) {
        for (EnlistmentStatus status : EnlistmentStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid ApprovalStatus value: " + value);
    }

}
