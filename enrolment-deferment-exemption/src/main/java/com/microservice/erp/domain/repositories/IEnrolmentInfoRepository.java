package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.dto.DashboardDto;
import com.microservice.erp.domain.entities.EnrolmentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import scala.Char;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;

/**
 * @author Rajib Kumer Ghosh
 */

@Repository
public interface IEnrolmentInfoRepository extends JpaRepository<EnrolmentInfo, BigInteger> {
    EnrolmentInfo findByUserId(BigInteger userId);

    List<EnrolmentInfo> findByYear(String year);

    List<EnrolmentInfo> findByYearAndUnderAge(String year, Character underAge);

    List<EnrolmentInfo> findByYearAndTrainingAcademyIdAndStatusAndGender(String year, Integer trainingAcademyId, Character status, Character gender);
}
