package com.microservice.erp.domain.helper;

import javax.validation.constraints.NotNull;

public enum ApprovalStatus {
    APPROVED('A',"Approved"),
    REJECTED('R',"Rejected"),
    PENDING('P',"Pending"),
    PENDING_APPROVAL('N',"Pending Approval"),
    PENDING_REJECTION('S',"Pending Rejection"),
    REVIEWED('T',"Reviewed"),
    REVERTED('V',"Reverted");

    private final char value;
    private final String name;

    ApprovalStatus(char value, String name) {
        this.value = value;
        this.name = name;
    }

    public char value() { // Removed @NotNull annotation for simplicity
        return value;
    }

    public String getName() { // Getter for the name
        return name;
    }

    public static ApprovalStatus fromValue(char value) {
        for (ApprovalStatus status : ApprovalStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid ApprovalStatus value: " + value);
    }
}
