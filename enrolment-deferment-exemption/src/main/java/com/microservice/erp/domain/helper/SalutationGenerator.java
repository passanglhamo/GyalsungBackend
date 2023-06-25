package com.microservice.erp.domain.helper;

import lombok.Getter;
import lombok.Setter;

public class SalutationGenerator {
    public static String getSalutation(Character gender) {
        return gender == 'M' ? "Mr. " : "Ms.";
    }

    public static String getPronoun(Character gender) {
        return gender == 'M' ? "his " : "her ";
    }
}
