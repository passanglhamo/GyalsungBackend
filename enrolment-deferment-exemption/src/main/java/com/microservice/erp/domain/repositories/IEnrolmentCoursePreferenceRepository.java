package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.EnrolmentCoursePreference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEnrolmentCoursePreferenceRepository extends JpaRepository<EnrolmentCoursePreference, Long> {
}
