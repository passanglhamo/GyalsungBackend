package com.microservice.erp.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name = "sa_access_permission")
@Setter
@Getter
@AttributeOverride(name = "id", column = @Column(name = "permission_id", columnDefinition = "bigint"))
public class RoleWiseAccessPermission extends Auditable<BigInteger, Long> {
    //region private variables
//    @Id
//    @Column(name = "permission_id", columnDefinition = "bigint")
//    private BigInteger permissionId;

    @Column(name = "role_id", columnDefinition = "bigint")
    private BigInteger roleId;

    @Column(name = "screen_id", columnDefinition = "bigint")
    private Integer screenId;

    @Column(name = "view_allowed", columnDefinition = "char(1)")
    private Character viewAllowed;

    @Column(name = "edit_allowed", columnDefinition = "char(1)")
    private Character editAllowed;

    @Column(name = "delete_allowed", columnDefinition = "char(1)")
    private Character deleteAllowed;

    @Column(name = "save_allowed", columnDefinition = "char(1)")
    private Character saveAllowed;

    //endregion

}
