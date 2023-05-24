package com.microservice.erp.domain.dao;

import com.microservice.erp.domain.helper.BaseDao;
import org.hibernate.query.NativeQuery;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigInteger;

@Repository
public class TrainingDateDao extends BaseDao {
    private final Environment environment;

    public TrainingDateDao(Environment environment) {
        this.environment = environment;
    }

    @Transactional
    public Character findByYear(Integer year) {
        String sqlQuery = environment.getProperty("CommonDao.findByYear");
        NativeQuery hQuery = (NativeQuery) hibernateQuery(sqlQuery)
                 .setParameter("year", year);
        return hQuery.list().isEmpty() ? null : (Character) hQuery.list().get(0);
    }

    @Transactional
    public Character findByStatusAndId(Character status, BigInteger trainingDateId) {
        String sqlQuery = environment.getProperty("CommonDao.findByStatusAndId");
        NativeQuery hQuery = (NativeQuery) hibernateQuery(sqlQuery)
                .setParameter("status", status)
                .setParameter("trainingDateId", trainingDateId);
        return hQuery.list().isEmpty() ? null : (Character) hQuery.list().get(0);
    }

    @Transactional
    public Character findByYearAndId(Integer newYear, BigInteger trainingDateId) {
        String sqlQuery = environment.getProperty("CommonDao.findByYearAndId");
        NativeQuery hQuery = (NativeQuery) hibernateQuery(sqlQuery)
                .setParameter("year", newYear)
                .setParameter("trainingDateId", trainingDateId);
        return hQuery.list().isEmpty() ? null : (Character) hQuery.list().get(0);
    }
}
