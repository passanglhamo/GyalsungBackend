package com.microservice.erp.domain.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity(name = "tms_hos_schedule_time")
@AttributeOverride(name = "id", column = @Column(name = "hos_schedule_time_id"))
public class HospitalScheduleTime extends Auditable<Long, Long> {

    @NotNull(message = "Appointment start time cannot be null")
    @Basic(optional = false)
    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @NotNull(message = "Appointment end time cannot be null")
    @Basic(optional = false)
    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @NotNull()
    @Column(name = "book_status", columnDefinition = "char(1)")//A=Available, B=Booked, C=Checkup Done
    private Character bookStatus;

    @Column(name = "booked_by")//bookedBy=userId
    private Long bookedBy;

    @Column(name = "booked_date")
    private LocalDate bookedDate;

    @Column(name = "checkup_done_by")//checkUpDoneBy=userId which is doctor
    private Long checkupDoneBy;

    @ManyToOne
    @JoinColumn(name = "hospital_schedule_date_id", nullable = false)
    private HospitalScheduleDate hospitalScheduleDate;

}
