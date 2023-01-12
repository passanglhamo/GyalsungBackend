package com.microservice.erp.domain.models;

import com.infoworks.lab.rest.models.Response;
import com.infoworks.lab.rest.validation.Password.PasswordRule;

import javax.validation.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

public class ChangePassRequest extends Response {

    @PasswordRule(mixLengthRule = 8, maxLengthRule = 20)
    @NotEmpty(message = "oldPassword must not null or empty!")
    private String oldPassword;

    @PasswordRule(mixLengthRule = 8, maxLengthRule = 20)
    @NotEmpty(message = "newPassword must not null or empty!")
    private String newPassword;

    public ChangePassRequest() {}

    public ChangePassRequest(@NotEmpty(message = "oldPassword must not null or empty!") String oldPassword
            , @NotEmpty(message = "newPassword must not null or empty!") String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public static boolean applyPasswordRules(String password) throws ValidationException {
        ChangePassRequest request = new ChangePassRequest(password, password);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ChangePassRequest>> violations = validator.validate(request);
        if (violations.isEmpty()) return true;
        StringBuilder msg = new StringBuilder();
        for (ConstraintViolation<ChangePassRequest> violation : violations) {
            msg.append(violation.getMessage() + "\n");
        }
        throw new ValidationException(msg.toString());
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
