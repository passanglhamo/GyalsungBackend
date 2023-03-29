package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.EnrolmentCoursePreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IEnrolmentCoursePreferenceRepository extends JpaRepository<EnrolmentCoursePreference, BigInteger> {

}
