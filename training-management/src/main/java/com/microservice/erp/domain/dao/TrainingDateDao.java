package com.microservice.erp.domain.dao;

import com.microservice.erp.domain.helper.BaseDao;
import org.hibernate.query.NativeQuery;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingDateDao extends BaseDao {
    private final Environment environment;

    public TrainingDateDao(Environment environment) {
        this.environment = environment;
    }

    public Character findByYear(Integer year) {
        String sqlQuery = environment.getProperty("CommonDao.findByYear");
        NativeQuery hQuery = (NativeQuery) hibernateQuery(sqlQuery)
                 .setParameter("year", year);
        return hQuery.list().isEmpty() ? null : (Character) hQuery.list().get(0);
    }

}
