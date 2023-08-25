package com.microservice.erp.domain.helper;

public enum Gender {
    MALE('M', "Male"),
    FEMALE('F', "Female");

    private final char value;
    private final String name;

    Gender(char value, String name) {
        this.value = value;
        this.name = name;
    }

    public char value() { // Removed @NotNull annotation for simplicity
        return value;
    }

    public String getName() { // Getter for the name
        return name;
    }

    public static Gender fromValue(char value) {
        for (Gender status : Gender.values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid ApprovalStatus value: " + value);
    }
}
