package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.EnrolmentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

/**
 * @author Rajib Kumer Ghosh
 */

@Repository
public interface IEnrolmentInfoRepository extends JpaRepository<EnrolmentInfo, BigInteger> {


    EnrolmentInfo findByUserId(BigInteger userId);

    List<EnrolmentInfo> findByYear(String year);

    List<EnrolmentInfo> findByYearAndUnderAge(String year, Character underAge);

    List<EnrolmentInfo> findByYearAndTrainingAcademyIdAndStatusAndGender(String year, Integer trainingAcademyId, Character status, Character gender);

    List<EnrolmentInfo> findByYearAndStatusAndGenderOrderById(String year, Character status, Character gender);

    Optional<EnrolmentInfo> findByYearAndUserIdAndStatus(String year, BigInteger userId, Character status);

    List<EnrolmentInfo> findByYearAndTrainingAcademyId(String year, Integer trainingAcademyId);

    long countByYearAndTrainingAcademyIdAndStatusAndGender(String year, Integer trainingAcademyId,  Character status, Character gender);


    EnrolmentInfo findByUserIdAndYearAndStatus(BigInteger userId, String year, Character value);

    @Query("SELECT COUNT(e) FROM ede_enrolment_info e " +
            "WHERE e.status =:status AND e.gender =:gender AND e.year =:year " +
            "AND e.trainingAcademyId =:academyId")
    Long getCountByStatusAndGenderAndYearAndTrainingAcademyId(Character status, Character gender, String year,
                                                              Integer academyId);
}
