package com.microservice.erp.domain.repository;

import com.microservice.erp.domain.entities.TrainingDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface ITrainingDateRepository extends JpaRepository<TrainingDate, BigInteger> {
    TrainingDate findByStatus(Character status);
    TrainingDate findFirstByOrderByTrainingDateIdDesc();

    TrainingDate findByTrainingDateId(BigInteger trainingDateId);

    @Query(value = "select * from tms_training_date e where year(e.training_date) =:year", nativeQuery = true)
    TrainingDate findByYear(@Param("year") String year);
}
