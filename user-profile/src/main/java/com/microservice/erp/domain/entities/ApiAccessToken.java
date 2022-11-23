package com.microservice.erp.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigInteger;


@Entity(name = "api_access_token")
//@AttributeOverride(name = "id", column = @Column(name = "USERID"))
public class ApiAccessToken extends Auditable<BigInteger, Long> {
    //region private variables
    @Column(name = "access_token", columnDefinition = "varchar(255)")
    private String access_token;

    @Column(name = "scope", columnDefinition = "varchar(255)")
    private String scope;

    @Column(name = "token_type", columnDefinition = "varchar(255)")
    private String token_type;

    @Column(name = "expires_in", columnDefinition = "int")
    private Integer expires_in;

    @Column(name = "created_on", columnDefinition = "bigint")
    private BigInteger created_on;
    //endregion

    //region setters and getters
    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }

    public BigInteger getCreated_on() {
        return created_on;
    }

    public void setCreated_on(BigInteger created_on) {
        this.created_on = created_on;
    }
    //endregion
}