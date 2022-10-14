package com.microservice.erp.domain.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Rajib Kumer Ghosh
 *
 */

//@Entity(name = "SYS_USERPROFILE")
//@AttributeOverride(name = "id", column = @Column(name = "USERID"))
public class UserProfile extends Auditable<Long, Long> {

    @Id
    @Column(name = "USERID")
    private String userId;

    @Column(name = "FULLNAME")
    private String fullName;

    @Column(name = "GROUPID")
    private String groupId;

    @Column(name = "GROUPNAME")
    private String groupName;

    @Column(name = "STATUSID")
    private String statusId;

    public UserProfile() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

}
