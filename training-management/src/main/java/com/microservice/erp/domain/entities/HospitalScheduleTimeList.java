package com.microservice.erp.domain.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity(name = "tms_hos_schedule_time_list")
@AttributeOverride(name = "id", column = @Column(name = "hos_schedule_time_list_id"))
public class HospitalScheduleTimeList extends Auditable<Long, Long>{

    @NotNull(message = "Appointment start time cannot be null")
    @Basic(optional = false)
    @Column(name = "start_time",columnDefinition = "varchar(255)")
    private String startTime;

    @NotNull(message = "Appointment end time cannot be null")
    @Basic(optional = false)
    @Column(name = "end_time",columnDefinition = "varchar(255)")
    private String endTime;

    @ManyToOne
    @JoinColumn(name = "hospital_schedule_time_id", nullable = false)
    private HospitalScheduleTime hospitalScheduleTime;

}
