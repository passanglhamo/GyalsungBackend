package com.microservice.erp.domain.dao;

import com.microservice.erp.domain.dto.DashboardDto;
import com.microservice.erp.domain.helper.BaseDao;
import org.hibernate.SQLQuery;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.math.BigInteger;

@Repository
public class AutoExemptionDao extends BaseDao {

    private final Environment environment;

    public AutoExemptionDao(Environment environment) {
        this.environment = environment;
    }

    @Transactional
    public String isCidAlreadyExist(String cid, BigInteger id) {
        String sqlQuery = environment.getProperty("CommonDao.isCidAlreadyExist");
        NativeQuery hQuery = (NativeQuery) hibernateQuery(sqlQuery)
                .setParameter("cid", cid)
                .setParameter("id", id);
        return hQuery.list().isEmpty() ? null : (String) hQuery.list().get(0);
    }
}
