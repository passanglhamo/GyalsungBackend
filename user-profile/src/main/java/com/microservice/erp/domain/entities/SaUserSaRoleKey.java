package com.microservice.erp.domain.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.math.BigInteger;

@Embeddable
public class SaUserSaRoleKey implements Serializable {
    @Column(name = "user_id",columnDefinition = "bigint")
    BigInteger userId;

    @Column(name = "role_id",columnDefinition = "bigint")
    BigInteger roleId;

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public BigInteger getRoleId() {
        return roleId;
    }

    public void setRoleId(BigInteger roleId) {
        this.roleId = roleId;
    }
}
