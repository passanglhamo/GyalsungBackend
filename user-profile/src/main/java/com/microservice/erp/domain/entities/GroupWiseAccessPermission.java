package com.microservice.erp.domain.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;


@Entity(name = "sa_group_wise_permission")
@AttributeOverride(name = "id", column = @Column(name = "permission_id", columnDefinition = "bigint"))
 public class GroupWiseAccessPermission extends Auditable<BigInteger, Long> {

    @NotNull
    @Column(name = "group_id", columnDefinition = "bigint")
    private BigInteger groupId;

    @NotNull
    @Column(name = "screen_id", columnDefinition = "int")
    private Integer screenId;

    @NotNull
    @Column(name = "has_view_permission", columnDefinition = "char(1)")
    private Character hasViewPermission;

    @NotNull
    @Column(name = "has_add_permission", columnDefinition = "char(1)")
    private Character hasAddPermission;

    @NotNull
    @Column(name = "has_edit_permission", columnDefinition = "char(1)")
    private Character hasEditPermission;

    @NotNull
    @Column(name = "has_delete_permission", columnDefinition = "char(1)")
    private Character hasDeletePermission;

    public BigInteger getGroupId() {
        return groupId;
    }

    public void setGroupId(BigInteger groupId) {
        this.groupId = groupId;
    }

    public Integer getScreenId() {
        return screenId;
    }

    public void setScreenId(Integer screenId) {
        this.screenId = screenId;
    }

    public Character getHasViewPermission() {
        return hasViewPermission;
    }

    public void setHasViewPermission(Character hasViewPermission) {
        this.hasViewPermission = hasViewPermission;
    }

    public Character getHasAddPermission() {
        return hasAddPermission;
    }

    public void setHasAddPermission(Character hasAddPermission) {
        this.hasAddPermission = hasAddPermission;
    }

    public Character getHasEditPermission() {
        return hasEditPermission;
    }

    public void setHasEditPermission(Character hasEditPermission) {
        this.hasEditPermission = hasEditPermission;
    }

    public Character getHasDeletePermission() {
        return hasDeletePermission;
    }

    public void setHasDeletePermission(Character hasDeletePermission) {
        this.hasDeletePermission = hasDeletePermission;
    }
}
