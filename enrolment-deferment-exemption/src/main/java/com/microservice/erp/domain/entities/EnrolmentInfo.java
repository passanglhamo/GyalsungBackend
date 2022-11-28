package com.microservice.erp.domain.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;
import java.util.Set;

@Entity(name = "ede_enrolment_info")
@AttributeOverride(name = "id", column = @Column(name = "enrolment_id", columnDefinition = "bigint"))
public class EnrolmentInfo extends Auditable<BigInteger, Long> {

    //region private variables
    @NotNull(message = "User id cannot be null")
    @Column(name = "user_id", columnDefinition = "bigint")
    private BigInteger userId;

    @NotNull()
    @Column(name = "sex", columnDefinition = "char(1)")
    private Character sex;

    @Column(name = "under_age", columnDefinition = "char(1)")
    private Character underAge;
    @Column(name = "year", columnDefinition = "char(4)")
    private String year;
    @Column(name = "training_academy_id", columnDefinition = "int")
    private Integer trainingAcademyId;

    @NotNull(message = "Enrolled on cannot be null")
    @Column(name = "enrolled_on")
    private Date enrolledOn;

    @Column(name = "remarks", columnDefinition = "varchar(255)")
    private String remarks;

    @NotNull
    @Column(name = "status", columnDefinition = "char(1)")
    private Character status;

    @OneToMany(
            mappedBy = "enrolment",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<EnrolmentCoursePreference> enrolmentCoursePreferences;
    //endregion

    //region setters and getters

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public Character getUnderAge() {
        return underAge;
    }

    public void setUnderAge(Character underAge) {
        this.underAge = underAge;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getTrainingAcademyId() {
        return trainingAcademyId;
    }

    public void setTrainingAcademyId(Integer trainingAcademyId) {
        this.trainingAcademyId = trainingAcademyId;
    }

    public Date getEnrolledOn() {
        return enrolledOn;
    }

    public void setEnrolledOn(Date enrolledOn) {
        this.enrolledOn = enrolledOn;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public Set<EnrolmentCoursePreference> getEnrolmentCoursePreferences() {
        return enrolmentCoursePreferences;
    }

    public void setEnrolmentCoursePreferences(Set<EnrolmentCoursePreference> enrolmentCoursePreferences) {
        this.enrolmentCoursePreferences = enrolmentCoursePreferences;
    }

    //endregion
}
