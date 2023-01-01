package com.microservice.erp.domain.models;

import com.infoworks.lab.rest.models.Response;
import com.infoworks.lab.rest.validation.Password.PasswordRule;

import javax.validation.constraints.NotEmpty;

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
