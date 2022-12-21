package com.microservice.erp.controllers.rest;


import org.springframework.security.core.GrantedAuthority;

import java.math.BigInteger;
import java.util.List;


public class JwtResponse {
    private String accessToken;
    private String userId;
    private Object roles;
    private Object accessPermissions;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Object getRoles() {
        return roles;
    }

    public void setRoles(Object roles) {
        this.roles = roles;
    }

    public Object getAccessPermissions() {
        return accessPermissions;
    }

    public void setAccessPermissions(Object accessPermissions) {
        this.accessPermissions = accessPermissions;
    }
}
