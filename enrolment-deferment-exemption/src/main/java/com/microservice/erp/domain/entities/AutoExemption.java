package com.microservice.erp.domain.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigInteger;

@Entity(name = "ede_auto_exemption")
@AttributeOverride(name = "id", column = @Column(name = "auto_exemption_id", columnDefinition = "bigint"))
public class AutoExemption extends Auditable<BigInteger, Long> {

    //region private variables

    @Column(name = "cid", unique = true, columnDefinition = "varchar(255)")
    private String cid;

    @Column(name = "full_name", columnDefinition = "varchar(255)")
    private String fullName;

    @Column(name = "dob", columnDefinition = "varchar(255)")
    private String dob;

    @Column(name = "gender", columnDefinition = "varchar(255)")
    private String gender;
    //endregion

    //region setters and getters
    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    //endregion
}
