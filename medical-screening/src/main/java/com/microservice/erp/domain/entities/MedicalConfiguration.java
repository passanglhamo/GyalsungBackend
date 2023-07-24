package com.microservice.erp.domain.entities;

import com.opencsv.bean.CsvDate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "med_medical_configuration")
@AttributeOverride(name = "id", column = @Column(name = "medical_configuration_id", columnDefinition = "bigint"))
public class MedicalConfiguration extends Auditable<BigInteger, Long> {
    @NotNull
    @Basic(optional = false)
    @Column(name = "hospital_id", columnDefinition = "bigint")
    private Integer hospitalId;

    @NotNull(message = "Appointment date cannot be null")
    @Basic(optional = false)
    @Column(name = "appointment_date")
    private Date appointmentDate;

    @NotNull
    @Basic(optional = false)
    @Column(name = "am_slots", columnDefinition = "int")
    private Integer amSlots;

    @NotNull
    @Basic(optional = false)
    @Column(name = "pm_slots", columnDefinition = "int")
    private Integer pmSlots;

    @NotNull
    @Basic(optional = false)
    @Column(name = "status", columnDefinition = "char(1)")
    private Character status;

    public Integer getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Integer hospitalId) {
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
