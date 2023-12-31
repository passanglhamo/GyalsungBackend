package com.microservice.erp.domain.repository;

import com.microservice.erp.domain.entities.TrainingAcademy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;


@Repository
public interface ITrainingAcademyRepository extends JpaRepository<TrainingAcademy, Integer> {
    List<TrainingAcademy> findAllByOrderByNameAsc();
}
