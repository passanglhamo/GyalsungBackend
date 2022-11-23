package com.microservice.erp.domain.entities;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ede_enrolment_course_preference")
@AttributeOverride(name = "id", column = @Column(name = "enrolment_course_preference_id"))
public class EnrolmentCoursePreference extends Auditable<BigInteger, Long> {

    //region private variables
    @Column(name = "course_id")
    private BigInteger courseId;

    @Column(name = "user_id")
    private BigInteger userId;

    @NotNull(message = "Priority number cannot not be null")
    @Column(name = "preference_number")
    private Integer preferenceNumber;

    @ManyToOne
    @JoinColumn(name = "enrolment_id", nullable = false)
    private EnrolmentInfo enrolment;
    //endregion

    //region setters and getters

    public BigInteger getCourseId() {
        return courseId;
    }

    public void setCourseId(BigInteger courseId) {
        this.courseId = courseId;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public Integer getPreferenceNumber() {
        return preferenceNumber;
    }

    public void setPreferenceNumber(Integer preferenceNumber) {
        this.preferenceNumber = preferenceNumber;
    }

    public EnrolmentInfo getEnrolment() {
        return enrolment;
    }

    public void setEnrolment(EnrolmentInfo enrolment) {
        this.enrolment = enrolment;
    }

    //endregion
}
