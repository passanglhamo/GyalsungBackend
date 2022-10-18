package com.microservice.erp.domain.repository;

import com.microservice.erp.domain.entities.DzongkhagHospitalMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
/**
 * @author Passang Lhamo
 *
 */
@CrossOrigin(origins = "*")
@Repository
public interface IDzongkhagHospitalMappingRepository extends JpaRepository<DzongkhagHospitalMapping,Long> {
}
