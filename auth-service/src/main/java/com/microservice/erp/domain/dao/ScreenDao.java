package com.microservice.erp.domain.dao;

import com.microservice.erp.domain.helper.BaseDao;
import org.hibernate.query.NativeQuery;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigInteger;

@Repository
public class ScreenDao extends BaseDao {
    private final Environment environment;

    public ScreenDao(Environment environment) {
        this.environment = environment;
    }

    @Transactional
    public String isScreenIdAlreadyExist(Integer screenId, BigInteger id) {
        String sqlQuery = environment.getProperty("CommonDao.isScreenIdAlreadyExist");
        NativeQuery hQuery = (NativeQuery) hibernateQuery(sqlQuery)
                .setParameter("screenId", screenId)
                .setParameter("id", id);
        return hQuery.list().isEmpty() ? null : (String) hQuery.list().get(0);
    }

}
