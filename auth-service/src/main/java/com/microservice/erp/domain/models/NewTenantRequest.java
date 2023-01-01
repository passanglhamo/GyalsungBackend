package com.microservice.erp.domain.models;

import com.infoworks.lab.rest.models.Response;
import com.infoworks.lab.rest.validation.Email.EmailPattern;
import com.infoworks.lab.rest.validation.Password.PasswordRule;

import javax.validation.constraints.NotEmpty;

public class NewTenantRequest extends Response {

    @PasswordRule(mixLengthRule = 8, maxLengthRule = 20)
    @NotEmpty(message = "Password must not null or empty!")
    private String password;

    @EmailPattern(nullable = true, message = "invalid email address")
    private String email;

    private String mobile;

    public NewTenantRequest(String email, String password, String mobile) {
        this.email = email;
        this.password = password;
        this.mobile = mobile;
    }

    public NewTenantRequest() {}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
