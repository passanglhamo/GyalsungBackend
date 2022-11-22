package com.microservice.erp.domain.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode
@Entity(name = "tms_hospital_schedule_date")
@AttributeOverride(name = "id", column = @Column(name = "hospital_schedule_date_id"))
public class HospitalScheduleDate extends Auditable<Long, Long> {

    @NotNull
    @Basic(optional = false)
    @Column(name = "hospital_id")
    private Long hospitalId;

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

}
