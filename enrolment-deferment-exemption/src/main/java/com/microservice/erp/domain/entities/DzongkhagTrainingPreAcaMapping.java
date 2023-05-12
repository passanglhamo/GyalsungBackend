package com.microservice.erp.domain.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ede_dzongkhag_training_mapping")
@AttributeOverride(name = "id", column = @Column(name = "dzongkhag_hospital_training_id", columnDefinition = "bigint"))
public class DzongkhagTrainingPreAcaMapping extends Auditable<BigInteger, Long> {

    @NotNull
    @Basic(optional = false)
    @Column(name = "dzongkhag_id", columnDefinition = "int")
    private Integer dzongkhagId;


    @NotNull
    @Basic(optional = false)
    @Column(name = "first_preference", columnDefinition = "int")
    private Integer firstPreference;

    @NotNull
    @Basic(optional = false)
    @Column(name = "second_preference", columnDefinition = "int")
    private Integer secondPreference;

    @NotNull
    @Basic(optional = false)
    @Column(name = "third_preference", columnDefinition = "int")
    private Integer thirdPreference;

    @NotNull
    @Basic(optional = false)
    @Column(name = "fourth_preference", columnDefinition = "int")
    private Integer fourthPreference;

    @NotNull
    @Basic(optional = false)
    @Column(name = "fifth_preference", columnDefinition = "int")
    private Integer fifthPreference;

    public Integer getDzongkhagId() {
        return dzongkhagId;
    }

    public void setDzongkhagId(Integer dzongkhagId) {
        this.dzongkhagId = dzongkhagId;
    }


    public Integer getFirstPreference() {
        return firstPreference;
    }

    public void setFirstPreference(Integer firstPreference) {
        this.firstPreference = firstPreference;
    }

    public Integer getSecondPreference() {
        return secondPreference;
    }

    public void setSecondPreference(Integer secondPreference) {
        this.secondPreference = secondPreference;
    }

    public Integer getThirdPreference() {
        return thirdPreference;
    }

    public void setThirdPreference(Integer thirdPreference) {
        this.thirdPreference = thirdPreference;
    }

    public Integer getFourthPreference() {
        return fourthPreference;
    }

    public void setFourthPreference(Integer fourthPreference) {
        this.fourthPreference = fourthPreference;
    }

    public Integer getFifthPreference() {
        return fifthPreference;
    }

    public void setFifthPreference(Integer fifthPreference) {
        this.fifthPreference = fifthPreference;
    }
}
