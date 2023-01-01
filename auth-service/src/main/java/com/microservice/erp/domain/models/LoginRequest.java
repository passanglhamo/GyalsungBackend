package com.microservice.erp.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.infoworks.lab.rest.models.Response;
import com.infoworks.lab.rest.validation.Password.PasswordRule;

import javax.validation.constraints.NotEmpty;

public class LoginRequest extends Response {

    @NotEmpty(message = "Username must not null or empty!")
    private String username;

    @PasswordRule(mixLengthRule = 8, maxLengthRule = 20)
    @NotEmpty(message = "Password must not null or empty!")
    private String password;

    @JsonIgnore
    private long tokenTtl;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginRequest() {
    }

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

    public long getTokenTtl() {
        return tokenTtl;
    }

    public void setTokenTtl(long tokenTtl) {
        this.tokenTtl = tokenTtl;
    }
}
