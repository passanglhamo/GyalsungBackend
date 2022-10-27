package com.microservice.erp.domain.repository;

import com.microservice.erp.domain.entities.FieldSpecialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

/**
 * @author Rajib Kumer Ghosh
 */

@Repository
public interface IFieldSpecializationRepository extends JpaRepository<FieldSpecialization, Long> {
    List<FieldSpecialization> findAllByStatusOrderByFieldSpecNameAsc(Character status);

    List<FieldSpecialization> findAllByMathRequiredOrderByFieldSpecNameAsc(boolean defaultCourse);
    List<FieldSpecialization> findAllByDefaultCourseOrderByFieldSpecNameAsc(boolean defaultCourse);
}
