package com.microservice.erp.domain.dao;

import com.microservice.erp.domain.helper.BaseDao;
import org.hibernate.query.NativeQuery;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigInteger;

@Repository
public class SecuritySettingDao extends BaseDao {
    private final Environment environment;

    public SecuritySettingDao(Environment environment) {
        this.environment = environment;
    }

    @Transactional
    public void updatePassword(String pw, BigInteger id) {
        String sqlQuery = environment.getProperty("CommonDao.updatePassword");
        NativeQuery hQuery = (NativeQuery) hibernateQuery(sqlQuery)
                .setParameter("pw", pw)
                .setParameter("id", id);
        hQuery.executeUpdate();
    }
}
