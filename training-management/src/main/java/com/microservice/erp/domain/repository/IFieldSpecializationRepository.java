package com.microservice.erp.domain.repository;

import com.microservice.erp.domain.entities.FieldSpecialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

/**
 * @author Rajib Kumer Ghosh
 */
@CrossOrigin(origins = "*")
@Repository
public interface IFieldSpecializationRepository extends JpaRepository<FieldSpecialization, Long> {
    List<FieldSpecialization> findAllByStatusOrderByNameAsc(Character status);

    List<FieldSpecialization> findAllByMathRequiredOrderByNameAsc(boolean defaultCourse);
    List<FieldSpecialization> findAllByDefaultCourseOrderByNameAsc(boolean defaultCourse);
}
