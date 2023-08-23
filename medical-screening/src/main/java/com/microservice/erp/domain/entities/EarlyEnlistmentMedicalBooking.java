package com.microservice.erp.domain.entities;

import com.microservice.erp.domain.helper.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;

@Entity(name = "med_early_enlistment_medical_booking")
public class EarlyEnlistmentMedicalBooking extends BaseEntity {
    //region private variables
    @Id
    @NotNull
    @Basic(optional = false)
    @Column(name = "hospital_booking_id", columnDefinition = "bigint")
    private BigInteger hospitalBookingId;

    @NotNull
    @Basic(optional = false)
    @Column(name = "early_enlistment_id", columnDefinition = "bigint")
    private BigInteger earlyEnlistmentId;

    @NotNull
    @Basic(optional = false)
    @Column(name = "hospital_id", columnDefinition = "bigint")
    private BigInteger hospitalId;

    @NotNull(message = "Appointment date cannot be null")
    @Basic(optional = false)
    @Column(name = "appointment_date")
    private Date appointmentDate;

    @NotNull
    @Basic(optional = false)
    @Column(name = "am_pm", columnDefinition = "char(1)")
    private Character amPm;

    @NotNull
    @Basic(optional = false)
    @Column(name = "user_id", columnDefinition = "bigint")
    private BigInteger userId;

    @NotNull(message = "Full name cannot be null")
    @Column(name = "full_name", columnDefinition = "varchar(255)")
    private String fullName;

    @Column(name = "cid", columnDefinition = "varchar(255)")
    private String cid;

    @Column(name = "gender", columnDefinition = "char(1)")
    private Character gender;

    @Column(name = "dob")
    private Date dob;
    //endregion

    //region setters and getters
    public BigInteger getHospitalBookingId() {
        return hospitalBookingId;
    }

    public void setHospitalBookingId(BigInteger hospitalBookingId) {
        this.hospitalBookingId = hospitalBookingId;
    }

    public BigInteger getEarlyEnlistmentId() {
        return earlyEnlistmentId;
    }

    public void setEarlyEnlistmentId(BigInteger earlyEnlistmentId) {
        this.earlyEnlistmentId = earlyEnlistmentId;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }
    //endregion
}
