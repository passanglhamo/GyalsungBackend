package com.microservice.erp.domain.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.persistence.AttributeOverride;
import java.util.Date;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@Entity(name = "SYS_USERGROUP")
@AttributeOverride(name = "id", column = @Column(name = "GROUPID"))
public class UserGroup extends Auditable<Long, Long> {

    @NotNull
    @Size(max = 50)
    @Basic(optional = false)
    @Column(name = "GROUPNAME")
    private String groupName;

    @Basic(optional = false)
    @Column(name = "GROUPCREATIONDATE")
    @Temporal(TemporalType.DATE)
    private Date groupCreationDate;

    @Column(name = "VERSION")
    private Integer versionNo;

    public UserGroup() {
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Date getGroupCreationDate() {
        return groupCreationDate;
    }

    public void setGroupCreationDate(Date groupCreationDate) {
        this.groupCreationDate = groupCreationDate;
    }

    public Integer getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Integer versionNo) {
        this.versionNo = versionNo;
    }
}
