package com.microservice.erp.domain.entities;

import com.microservice.erp.domain.helper.EnumRole;

import javax.persistence.*;

@Entity
@Table(name = "sa_roles")
public class SaRole {
    @Id
    @Column(name = "role_id", columnDefinition = "int")
    private Integer roleId;
    @Column(name = "role_name", columnDefinition = "varchar(255)")
    private String roleName;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
