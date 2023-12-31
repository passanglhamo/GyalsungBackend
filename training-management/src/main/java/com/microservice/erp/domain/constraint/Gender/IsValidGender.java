package com.microservice.erp.domain.constraint.Gender;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = GenderConstraint.class)
public @interface IsValidGender {
    String message() default "e.g. MALE or FEMALE or TRANSGENDER or NONE";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
