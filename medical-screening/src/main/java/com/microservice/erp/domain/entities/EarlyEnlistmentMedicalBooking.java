package com.microservice.erp.domain.entities;

import com.microservice.erp.domain.helper.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;

@Entity(name = "med_early_enlistment_medical_booking")
public class EarlyEnlistmentMedicalBooking extends BaseEntity {
    @Id
    @NotNull
    @Basic(optional = false)
    @Column(name = "hospital_booking_id" , columnDefinition = "bigint")
    private BigInteger hospitalBookingId;

    @NotNull
    @Basic(optional = false)
    @Column(name = "early_enlistment_id" , columnDefinition = "bigint")
    private BigInteger earlyEnlistmentId;

    @NotNull
    @Basic(optional = false)
    @Column(name = "hospital_id" , columnDefinition = "bigint")
    private BigInteger hospitalId;

    @NotNull(message = "Appointment date cannot be null")
    @Basic(optional = false)
    @Column(name = "appointment_date")
    private Date appointmentDate;

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

    public BigInteger getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(BigInteger hospitalId) {
        this.hospitalId = hospitalId;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
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

    public BigInteger getEarlyEnlistmentId() {
        return earlyEnlistmentId;
    }

    public void setEarlyEnlistmentId(BigInteger earlyEnlistmentId) {
        this.earlyEnlistmentId = earlyEnlistmentId;
    }
}
