package com.microservice.erp.domain.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@Entity(name = "SYS_USERWISEGROUPMAP")
@IdClass(UserWiseGroupMap.UserWiseGroupMapPK.class)
//@AttributeOverride(name = "id", column = @Column(name = "GROUPID"))
public class UserWiseGroupMap {
    public static class UserWiseGroupMapPK implements Serializable {

        private static final long serialVersionUID = 1L;

        private String userId;
        private String groupId;

        public UserWiseGroupMapPK() {
        }

        public UserWiseGroupMapPK(String userId, String groupId) {
            this.userId = userId;
            this.groupId = groupId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        @Override
        public boolean equals(final Object obj) {
            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }

    @Id
    @NotNull
    @Basic(optional = false)
    @Column(name = "USERID")
    private String userId;

    @Id
    @NotNull
    @Basic(optional = false)
    @Column(name = "GROUPID")
    private String groupId;


    @Basic(optional = false)
    @Column(name = "GROUPNAME")
    private String groupName;

    public UserWiseGroupMap() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
}
