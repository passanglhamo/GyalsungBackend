package com.microservice.erp.domain.dao;

import com.microservice.erp.domain.helper.BaseDao;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Repository
public class AcademyWiseInfoDao extends BaseDao {

    private final Environment environment;

    public AcademyWiseInfoDao(Environment environment) {
        this.environment = environment;
    }

    @Transactional
    public BigInteger getEnrolmentFigureByYear(String year, char gender, Integer trainingAcaId) {
        String sqlQuery = environment.getProperty("CommonDao.getEnrolmentFigureByYear");
        return (BigInteger) hibernateQuery(sqlQuery).setParameter("year", year).setParameter("gender", gender).setParameter("trainingAcaId", trainingAcaId).uniqueResult();
    }
}
