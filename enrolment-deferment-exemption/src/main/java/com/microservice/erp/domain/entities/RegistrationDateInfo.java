package com.microservice.erp.domain.entities;

import com.microservice.erp.domain.helper.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ede_registration_date_info")
//@AttributeOverride(name = "id", column = @Column(name = "registration_date_id", columnDefinition = "bigint"))
public class RegistrationDateInfo extends BaseEntity {

    //region private variables
    @Id
    @Column(name = "registration_date_id", columnDefinition = "bigint")
    private BigInteger registrationDateId;

    @NotNull
    @Basic(optional = false)
    @Column(name = "registration_year", columnDefinition = "char(4)")
    private String registrationYear;

    @Column(name = "from_date")
    @Temporal(TemporalType.DATE)
    private Date fromDate = new java.sql.Date(new Date().getTime());

    @NotNull(message = "Till date cannot be null")
    @Basic(optional = false)
    @Column(name = "to_date")
    @Temporal(TemporalType.DATE)
    private Date toDate = new java.sql.Date(new Date().getTime());

    @NotNull
    @Basic(optional = false)
    @Column(name = "status", columnDefinition = "char(1)")
    private Character status;
    //endregion

    //region setters and getters

    public BigInteger getRegistrationDateId() {
        return registrationDateId;
    }

    public void setRegistrationDateId(BigInteger registrationDateId) {
        this.registrationDateId = registrationDateId;
    }

    public String getRegistrationYear() {
        return registrationYear;
    }

    public void setRegistrationYear(String registrationYear) {
        this.registrationYear = registrationYear;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }
    //endregion

}
