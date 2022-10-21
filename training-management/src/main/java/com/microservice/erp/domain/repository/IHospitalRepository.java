package com.microservice.erp.domain.repository;

import com.microservice.erp.domain.entities.Dzongkhag;
import com.microservice.erp.domain.entities.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Collection;
import java.util.List;


@Repository
public interface IHospitalRepository extends JpaRepository<Hospital, Integer> {

    @Query(value = "select * from tms_dzongkhag_hospital_mapping m inner join \n" +
            "public.st_hospital h on h.hospital_id = m.hospital_id \n" +
            "where m.dzongkhag_id =:dzongkhagId and h.status='A'", nativeQuery = true)
    List<Hospital> getAllActiveHospitalsByDzongkhagId(Integer dzongkhagId);
}
