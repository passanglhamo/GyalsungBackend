package com.microservice.erp.domain.entities;

//@Entity(name = "ede_auto_exemption")
//@AttributeOverride(name = "id", column = @Column(name = "auto_exemption_id", columnDefinition = "bigint"))
public class AutomaticExemption {

    //region private variables

//    @Column(name = "cid", columnDefinition = "varchar(255)")
    private String cid;

//    @Column(name = "dob", columnDefinition = "varchar(255)")
    private String dob;

//    @Column(name = "full_name", columnDefinition = "varchar(255)")
    private String fullName;
    //endregion

    //region setters and getters

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    //endregion
}
