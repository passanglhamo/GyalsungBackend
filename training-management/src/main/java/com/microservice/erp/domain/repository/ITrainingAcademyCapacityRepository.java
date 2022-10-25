package com.microservice.erp.domain.repository;

import com.microservice.erp.domain.entities.TrainingAcademyCapacity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

/**
 * @author Rajib Kumer Ghosh
 */

@Repository
public interface ITrainingAcademyCapacityRepository extends JpaRepository<TrainingAcademyCapacity, Long> {
    TrainingAcademyCapacity findByTrainingYear(String trainingYear);

    List<TrainingAcademyCapacity> findAll();
}
