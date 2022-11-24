package com.microservice.erp.domain.repository;

import com.microservice.erp.domain.entities.TrainingAcademyCapacity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface ITrainingAcademyCapacityRepository extends JpaRepository<TrainingAcademyCapacity, BigInteger> {
    TrainingAcademyCapacity findByTrainingYearAndAcademyId(String trainingYear, Integer academyId);

    List<TrainingAcademyCapacity> findAll();

    TrainingAcademyCapacity findByTrainingYearAndAcademyIdAndIdNot(String trainingYear, Integer academyId,BigInteger id);

}
