package com.microservice.erp.domain.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode
@Entity(name = "ede_deferment_info")
@AttributeOverride(name = "id", column = @Column(name = "deferment_id"))
public class DefermentInfo extends Auditable<Long, Long> {

    @NotNull(message = "User id cannot be null")
    @Basic(optional = false)
    @Column(name = "user_id")
    private Long userId;

//    @NotNull
//    @Basic(optional = false)
//    @Column(name = "deferment_year")
//    private String defermentYear;

    @Column(name = "from_date")
    @Temporal(TemporalType.DATE)
    private Date fromDate = new java.sql.Date(new Date().getTime());

    @NotNull(message = "Till date cannot be null")
    //@Future(message = "Till Date Of Schedule Must Be Greater Then Now")
    @Basic(optional = false)
    @Column(name = "to_date")
    @Temporal(TemporalType.DATE)
    private Date toDate = new java.sql.Date(new Date().getTime());

    @Basic(optional = false)
    @NotNull(message = "Reasons cannot be null")
    @Column(name = "reason_id",columnDefinition = "varchar(255)")
    private Long reasonId;

    @Column(name = "approval_remarks",columnDefinition = "varchar(255)")
    private String approvalRemarks;

    @Column(name = "remarks",columnDefinition = "varchar(255)")
    private String remarks;

    @NotNull
    @Basic(optional = false)
    @Column(name = "status",columnDefinition = "char(1)")
    private Character status;

    @OneToMany(
            mappedBy = "deferment",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<DefermentFileInfo> files;
}
