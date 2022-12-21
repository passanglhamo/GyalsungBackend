package com.microservice.erp.domain.entities;

import javax.persistence.*;
import java.math.BigInteger;

@Entity(name = "sa_roles")
@AttributeOverride(name = "id", column = @Column(name = "role_id", columnDefinition = "bigint"))
public class SaRole extends Auditable<BigInteger, Long> {

    //region private variables
    @Column(name = "role_name", columnDefinition = "varchar(255)")
    private String roleName;

    @Column(name = "is_open_user", columnDefinition = "char(1)")
    private Character isOpenUser;


    //endregion

    //region setters and getters

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Character getIsOpenUser() {
        return isOpenUser;
    }

    public void setIsOpenUser(Character isOpenUser) {
        this.isOpenUser = isOpenUser;
    }

    //endregion
}
