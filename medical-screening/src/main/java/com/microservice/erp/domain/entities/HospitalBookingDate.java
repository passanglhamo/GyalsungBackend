package com.microservice.erp.domain.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tms_hospital_booking_date")
@AttributeOverride(name = "id", column = @Column(name = "hospital_booking_date_id", columnDefinition = "bigint"))
public class HospitalBookingDate extends Auditable<BigInteger, Long>{
    @NotNull
    @Basic(optional = false)
    @Column(name = "hospital_id" , columnDefinition = "bigint")
    private BigInteger hospitalId;

    @NotNull(message = "Appointment date cannot be null")
    @Basic(optional = false)
    @Column(name = "appointment_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date appointmentDate = new java.sql.Date(new Date().getTime());

    @NotNull
    @Basic(optional = false)
    @Column(name = "am_slots",columnDefinition = "int")
    private Integer amSlots;

    @NotNull
    @Basic(optional = false)
    @Column(name = "pm_slots",columnDefinition = "int")
    private Integer pmSlots;

    @NotNull
    @Basic(optional = false)
    @Column(name = "status",columnDefinition = "char(1)")
    private Character status;

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

    public Integer getAmSlots() {
        return amSlots;
    }

    public void setAmSlots(Integer amSlots) {
        this.amSlots = amSlots;
    }

    public Integer getPmSlots() {
        return pmSlots;
    }

    public void setPmSlots(Integer pmSlots) {
        this.pmSlots = pmSlots;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }
}
