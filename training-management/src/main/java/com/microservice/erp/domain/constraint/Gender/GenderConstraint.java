package com.microservice.erp.domain.constraint.Gender;

import com.microservice.erp.domain.models.Gender;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Rajib Kumer Ghosh
 *
 */

public class GenderConstraint implements ConstraintValidator<IsValidGender, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        if (!value.isEmpty()){
            try {
                return Gender.valueOf(value) != null;
            } catch (IllegalArgumentException e) {}
        }
        return false;
    }
}
