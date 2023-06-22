package com.microservice.erp.domain.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tms_hospital_booking_details")
@AttributeOverride(name = "id", column = @Column(name = "hospital_booking_details_id", columnDefinition = "bigint"))
public class HospitalBookingDetail extends Auditable<BigInteger, Long>{
    @NotNull
    @Basic(optional = false)
    @Column(name = "hospital_booking_id" , columnDefinition = "bigint")
    private BigInteger hospitalBookingId;

    @NotNull
    @Basic(optional = false)
    @Column(name = "am_pm" , columnDefinition = "char(1)")
    private Character amPm;

    @NotNull
    @Basic(optional = false)
    @Column(name = "user_id" , columnDefinition = "bigint")
    private BigInteger userId;

    public BigInteger getHospitalBookingId() {
        return hospitalBookingId;
    }

    public void setHospitalBookingId(BigInteger hospitalBookingId) {
        this.hospitalBookingId = hospitalBookingId;
    }

    public Character getAmPm() {
        return amPm;
    }

    public void setAmPm(Character amPm) {
        this.amPm = amPm;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }
}
