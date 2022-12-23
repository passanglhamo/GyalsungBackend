package com.microservice.erp.domain.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigInteger;

@Entity(name = "sa_role")
@AttributeOverride(name = "id", column = @Column(name = "role_id", columnDefinition = "bigint"))
public class SaRole extends Auditable<BigInteger, Long> {

    //region private variables
    @Column(name = "role_name", columnDefinition = "varchar(255)")
    private String roleName;

    @Column(name = "is_open_user", columnDefinition = "char(1)")
    private String isOpenUser;

    //endregion

    //region setters and getters

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getIsOpenUser() {
        return isOpenUser;
    }

    public void setIsOpenUser(String isOpenUser) {
        this.isOpenUser = isOpenUser;
    }

    //endregion
}
