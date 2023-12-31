package com.microservice.erp.domain.dao;

import com.microservice.erp.domain.dto.EnrolmentListDto;
import com.microservice.erp.domain.helper.BaseDao;
import org.hibernate.query.NativeQuery;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Repository
public class EnrolmentDao extends BaseDao {

    private final Environment environment;

    public EnrolmentDao(Environment environment) {
        this.environment = environment;
    }

    @Transactional
    public List<EnrolmentListDto> getEnrolmentListByYearAndCoursePreference(String year, Character applicationStatus, BigInteger courseId,
                                                                            Integer coursePreferenceNumber) {
        String sqlQuery = environment.getProperty("CommonDao.getEnrolmentListByYearAndCoursePreference");
        NativeQuery hQuery = (NativeQuery) hibernateQuery(sqlQuery, EnrolmentListDto.class)
                .setParameter("year", year)
                .setParameter("applicationStatus", applicationStatus)
                .setParameter("courseId", courseId)
                .setParameter("coursePreferenceNumber", coursePreferenceNumber);
        return hQuery.list().isEmpty() ? null : hQuery.list();
    }

    @Transactional
    public List<EnrolmentListDto> getEnrolmentListByYearAndStatus(String year, Character applicationStatus) {
        String sqlQuery = environment.getProperty("CommonDao.getEnrolmentListByYearAndStatus");
        NativeQuery hQuery = (NativeQuery) hibernateQuery(sqlQuery, EnrolmentListDto.class)
                .setParameter("year", year)
                .setParameter("applicationStatus", applicationStatus);
        return hQuery.list().isEmpty() ? null : hQuery.list();
    }

    public List<EnrolmentListDto> getEnrolmentListByYearCourseAndAcademy(String year, Integer trainingAcademyId) {
        String sqlQuery = environment.getProperty("CommonDao.getEnrolmentListByYearCourseAndAcademy");
        NativeQuery hQuery = (NativeQuery) hibernateQuery(sqlQuery, EnrolmentListDto.class)
                .setParameter("year", year)
                .setParameter("trainingAcademyId", trainingAcademyId);
//                .setParameter("courseId", courseId);
        return hQuery.list().isEmpty() ? null : hQuery.list();
    }
}
