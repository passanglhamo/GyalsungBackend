package com.microservice.erp.domain.repository;

import com.microservice.erp.domain.entities.TrainingAcademyCapacity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface ITrainingAcademyCapacityRepository extends JpaRepository<TrainingAcademyCapacity, BigInteger> {
    Optional<TrainingAcademyCapacity> findByTrainingYearAndAcademyId(String trainingYear, Integer academyId);

    List<TrainingAcademyCapacity> findAll();

    TrainingAcademyCapacity findByTrainingYearAndAcademyIdAndIdNot(String trainingYear, Integer academyId,BigInteger id);

}
