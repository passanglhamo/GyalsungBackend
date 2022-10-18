package com.microservice.erp.domain.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Future;
import java.util.Date;
import java.util.Objects;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode
@Entity(name = "tms_enlistment_schedule")
@AttributeOverride(name = "id", column = @Column(name = "enlistment_schedule_id"))
public class EnlistmentSchedule extends Auditable<Long, Long> {

    /*@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ENLISTMENT_SCHEDULE_ID")
    private Long id;*/

    @NotNull(message = "From date cannot be null")
    @Future(message = "From date of schedule should be greater than current date")
    @Basic(optional = false)
    @Column(name = "from_date")
    @Temporal(TemporalType.DATE)
    private Date fromDate = new java.sql.Date(new Date().getTime());

    @NotNull(message = "To date cannot be null")
    @Future(message = "To date of schedule should be greater than current date")
    @Basic(optional = false)
    @Column(name = "to_date")
    @Temporal(TemporalType.DATE)
    private Date toDate = new java.sql.Date(new Date().getTime());

    @NotNull
    @Basic(optional = false)
    @Column(name = "status",columnDefinition = "char(1)")
    private String status;

}
