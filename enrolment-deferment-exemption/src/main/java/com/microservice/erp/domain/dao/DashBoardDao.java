package com.microservice.erp.domain.dao;

import com.microservice.erp.domain.dto.DashboardDto;
import com.microservice.erp.domain.helper.BaseDao;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

@Repository
public class DashBoardDao extends BaseDao {

    private final Environment environment;

    public DashBoardDao(Environment environment) {
        this.environment = environment;
    }

    @Transactional
    public DashboardDto getRegistrationInfoFigures(String year) {
        String sqlQuery = environment.getProperty("CommonDao.getRegistrationInfoFigures");
        try {
            return (DashboardDto) entityManager.createNativeQuery(sqlQuery)
//                     .setParameter("year", year)
                    .unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(DashboardDto.class))
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}