package com.microservice.erp.domain.entities;

import lombok.*;

import javax.persistence.*;
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
@Entity(name = "ede_exemption_info")
@AttributeOverride(name = "id", column = @Column(name = "exemption_id"))
public class ExemptionInfo extends Auditable<Long, Long> {

    @NotNull(message = "User id cannot be null")
    @Basic(optional = false)
    @Column(name = "user_id")
    private Long userId;

    @NotNull(message = "Application date cannot be null")
    @Basic(optional = false)
    @Column(name = "application_date")
    @Temporal(TemporalType.DATE)
    private Date applicationDate = new java.sql.Date(new Date().getTime());


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
            mappedBy = "exemption",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<ExemptionFileInfo> files;
}
