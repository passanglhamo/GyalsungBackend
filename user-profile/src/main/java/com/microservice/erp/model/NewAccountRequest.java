package com.microservice.erp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.infoworks.lab.rest.models.Response;
import com.infoworks.lab.rest.validation.Email.EmailPattern;
import com.infoworks.lab.rest.validation.Password.PasswordRule;

import javax.validation.constraints.NotEmpty;

public class NewAccountRequest extends Response {

    @NotEmpty(message = "Username must not null or empty!")
    private String username;

    @PasswordRule(mixLengthRule = 8, maxLengthRule = 20)
    @NotEmpty(message = "Password must not null or empty!")
    private String password;

    @EmailPattern(nullable = true, message = "invalid email address")
    private String email;

    private String mobile;

    @JsonIgnore
    private String role;

    public NewAccountRequest(String username, String password, String email, String mobile) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.mobile = mobile;
    }

    public NewAccountRequest() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
