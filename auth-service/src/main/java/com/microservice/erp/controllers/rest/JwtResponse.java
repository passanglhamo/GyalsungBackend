package com.microservice.erp.controllers.rest;


import java.math.BigInteger;
import java.util.List;


public class JwtResponse {
    private String accessToken;
    private String type = "Bearer";
    private BigInteger userId;
    private String username;
    private String fullName;
    private String cid;
    private Character gender;
    private String mobileNo;
    private String email;
    private List<String> roles;

    public String getAccessToken() {
        return accessToken;
    }

    public String getType() {
        return type;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getCid() {
        return cid;
    }

    public Character getGender() {
        return gender;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public JwtResponse(String accessToken, BigInteger userId, String fullName, String cid, Character gender,
                       String mobileNo, String username, String email, List<String> roles) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.fullName = fullName;
        this.cid = cid;
        this.gender = gender;
        this.mobileNo = mobileNo;
        this.email = email;
        this.username = username;
        this.roles = roles;
    }


}
