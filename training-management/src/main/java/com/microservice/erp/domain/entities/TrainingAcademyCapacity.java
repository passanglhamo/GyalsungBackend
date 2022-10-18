package com.microservice.erp.domain.entities;


import lombok.*;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * @author Rajib Kumer Ghosh
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EqualsAndHashCode
@Entity(name = "tms_training_academy_capacity")
@AttributeOverride(name = "id", column = @Column(name = "training_academy_capacity_id"))
public class TrainingAcademyCapacity extends Auditable<Long, Long> {

    @Basic(optional = false)
    @NotNull(message = "Name must not be null")
    @Column(name = "academy_id")
    private Integer academyId;

    @NotNull
    @Basic(optional = false)
    @Column(name = "training_year",columnDefinition = "char(4)")
    private String trainingYear;

    @Basic(optional = false)
    @NotNull(message = "Capacity must not be null")
    @Column(name = "male_capacity_amount")
    private Integer maleCapacityAmount;

    @Basic(optional = false)
    @NotNull(message = "Capacity must not be null")
    @Column(name = "female_capacity_amount")
    private Integer femaleCapacityAmount;


    @NotNull
    @Basic(optional = false)
    @Column(name = "status")
    private String status;

}
