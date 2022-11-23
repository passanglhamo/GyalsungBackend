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
    public List<EnrolmentListDto> getEnrolmentListByYearAndCoursePreference(String year, BigInteger courseId,
                                                                            Integer coursePreferenceNumber) {
        String sqlQuery = environment.getProperty("CommonDao.getEnrolmentListByYearAndCoursePreference");
        NativeQuery hQuery = (NativeQuery) hibernateQuery(sqlQuery, EnrolmentListDto.class)
                .setParameter("year", year)
                .setParameter("courseId", courseId)
                .setParameter("coursePreferenceNumber", coursePreferenceNumber);
        return hQuery.list().isEmpty() ? null : hQuery.list();
    }

}
