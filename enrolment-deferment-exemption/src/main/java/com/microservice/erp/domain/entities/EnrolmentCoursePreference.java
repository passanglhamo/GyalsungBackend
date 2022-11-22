package com.microservice.erp.domain.entities;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ede_enrolment_course_preference")
@AttributeOverride(name = "id", column = @Column(name = "enrolment_course_preference_id"))
public class EnrolmentCoursePreference extends Auditable<Long, Long> {

    //region private variables
    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "user_id")
    private Long userId;

    @NotNull(message = "Priority number cannot not be null")
    @Column(name = "preference_number")
    private Integer preferenceNumber;

    @ManyToOne
    @JoinColumn(name = "enrolment_id", nullable = false)
    private EnrolmentInfo enrolment;
    //endregion

    //region setters and getters

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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
