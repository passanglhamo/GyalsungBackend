package com.microservice.erp.controllers.rest;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

@Setter
@Getter
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
