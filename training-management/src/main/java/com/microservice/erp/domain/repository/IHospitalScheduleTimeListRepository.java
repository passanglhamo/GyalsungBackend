package com.microservice.erp.domain.repository;

import com.microservice.erp.domain.entities.HospitalScheduleTimeList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;


@Repository
public interface IHospitalScheduleTimeListRepository extends JpaRepository<HospitalScheduleTimeList,Long> {
}
