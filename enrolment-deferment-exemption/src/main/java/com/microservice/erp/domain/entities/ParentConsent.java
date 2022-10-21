package com.microservice.erp.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "ede_parent_consent")
@AttributeOverride(name = "id", column = @Column(name = "parent_consent_id"))
public class ParentConsent extends Auditable<Long, Long> {

    @Basic(optional = false)
    @NotNull(message = "User Id not be null")
    @Column(name = "user_id")
    private Long userId;

    @Basic(optional = false)
    @NotNull(message = "Parent/Guardian name not be null")
    @Column(name = "guardian_name", columnDefinition = "varchar(255)")
    private String guardianName;

    @Basic(optional = false)
    @NotNull(message = "Parent/Guardian mobile number cannot not be null")
    @Column(name = "guardian_mobile_no", columnDefinition = "varchar(255)")
    private String guardianMobileNo;

}
