package com.microservice.erp.domain.repository;

import com.microservice.erp.domain.entities.Dzongkhag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;


@Repository
public interface IDzongkhagRepository extends JpaRepository<Dzongkhag, Integer> {
    List<Dzongkhag> findAllByOrderByDzongkhagNameAsc();

    Dzongkhag findByDzongkhagId(Integer presentDzongkhagId);

    @Query(value = "select d.* from st_dzongkhag d inner join tms_dzongkhag_hospital_mapping m \n" +
            "on d.dzongkhag_id = m.dzongkhag_id where m.hospital_id =:hospitalId", nativeQuery = true)
    Dzongkhag getHospitalMappingByHospitalId(Integer hospitalId);
}
