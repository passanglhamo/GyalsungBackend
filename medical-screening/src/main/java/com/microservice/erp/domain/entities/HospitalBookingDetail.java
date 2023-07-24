package com.microservice.erp.domain.entities;

import com.microservice.erp.domain.helper.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tms_hospital_booking_details")
@AttributeOverride(name = "id", column = @Column(name = "hospital_booking_details_id", columnDefinition = "bigint"))
public class HospitalBookingDetail extends BaseEntity {

    @Id
    @NotNull
    @Basic(optional = false)
    @Column(name = "hospital_booking_details_id", columnDefinition = "bigint")
    private BigInteger hospitalBookingDetailId;

    @NotNull
    @Basic(optional = false)
    @Column(name = "hospital_booking_id", columnDefinition = "bigint")
    private BigInteger hospitalBookingId;

    @NotNull
    @Basic(optional = false)
    @Column(name = "am_pm", columnDefinition = "char(1)")
    private Character amPm;

    @NotNull
    @Basic(optional = false)
    @Column(name = "user_id", columnDefinition = "bigint")
    private BigInteger userId;

    @NotNull
    @Basic(optional = false)
    @Column(name = "status", columnDefinition = "char(1)")
    private Character status;

    public BigInteger getHospitalBookingDetailId() {
        return hospitalBookingDetailId;
    }

    public void setHospitalBookingDetailId(BigInteger hospitalBookingDetailId) {
        this.hospitalBookingDetailId = hospitalBookingDetailId;
    }

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

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }
}
