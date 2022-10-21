package com.microservice.erp.domain.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

/**
 * @author Rajib Kumer Ghosh
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode
@Entity(name = "ede_enrolment_info")
@AttributeOverride(name = "id", column = @Column(name = "enrolment_id"))
public class EnrolmentInfo extends Auditable<Long, Long> {

    @NotNull(message = "User id cannot be null")
    @Basic(optional = false)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "training_academy_id")
    private Integer trainingAcademyId;

    @NotNull(message = "Start date cannot be null")
    @Future(message = "Start date of schedule must be greater than now")
    @Basic(optional = false)
    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate = new java.sql.Date(new Date().getTime());

    @NotNull(message = "End date cannot be null")
    @Future(message = "End date of schedule must be greater than now")
    @Basic(optional = false)
    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate = new java.sql.Date(new Date().getTime());

    @Column(name = "remarks", columnDefinition = "varchar(255)")
    private String remarks;

    @NotNull
    @Basic(optional = false)
    @Column(name = "status", columnDefinition = "char(1)")
    private Character status;

    @OneToMany(
            mappedBy = "enrolment",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<EnrolmentFileInfo> files;
}
