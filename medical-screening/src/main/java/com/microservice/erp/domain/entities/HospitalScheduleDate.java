package com.microservice.erp.domain.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "tms_hospital_schedule_date")
@AttributeOverride(name = "id", column = @Column(name = "hospital_schedule_date_id"))
public class HospitalScheduleDate extends Auditable<BigInteger, Long> {

    @NotNull
    @Basic(optional = false)
    @Column(name = "hospital_id")
    private BigInteger hospitalId;

    @NotNull(message = "Appointment date cannot be null")
    @Basic(optional = false)
    @Column(name = "appointment_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date appointmentDate = new java.sql.Date(new Date().getTime());

    @NotNull
    @Basic(optional = false)
    @Column(name = "status",columnDefinition = "char(1)")
    private Character status;

    @OneToMany(
            mappedBy = "hospitalScheduleDate",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<HospitalScheduleTime> hospitalScheduleTimeLists;

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

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public Set<HospitalScheduleTime> getHospitalScheduleTimeLists() {
        return hospitalScheduleTimeLists;
    }

    public void setHospitalScheduleTimeLists(Set<HospitalScheduleTime> hospitalScheduleTimeLists) {
        this.hospitalScheduleTimeLists = hospitalScheduleTimeLists;
    }
}
