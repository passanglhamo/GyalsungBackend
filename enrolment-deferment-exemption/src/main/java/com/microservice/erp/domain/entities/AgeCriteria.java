package com.microservice.erp.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Entity(name = "ede_age_criteria")
@AttributeOverride(name = "id", column = @Column(name = "age_criteria_id", columnDefinition = "bigint"))
public class AgeCriteria extends Auditable<BigInteger, Long> {
    //region private variables
    @NotNull(message = "Minimum age cannot be null")
    @Column(name = "minimum_age", columnDefinition = "int")
    private BigInteger minimumAge;

    @NotNull(message = "Maximum age cannot be null")
    @Column(name = "maximum_age", columnDefinition = "int")
    private BigInteger maximumAge;
    //endregion

    //region setters and getters
    public BigInteger getMinimumAge() {
        return minimumAge;
    }

    public void setMinimumAge(BigInteger minimumAge) {
        this.minimumAge = minimumAge;
    }

    public BigInteger getMaximumAge() {
        return maximumAge;
    }

    public void setMaximumAge(BigInteger maximumAge) {
        this.maximumAge = maximumAge;
    }

    //endregion
}
