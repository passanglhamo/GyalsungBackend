package com.microservice.erp.domain.dao;

import com.microservice.erp.domain.dto.ParentConsentDto;
import com.microservice.erp.domain.helper.BaseDao;
import org.hibernate.query.NativeQuery;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ParentConsentDao extends BaseDao {
    private final Environment environment;

    public ParentConsentDao(Environment environment) {
        this.environment = environment;
    }

    @Transactional
    public List<ParentConsentDto> getEligibleParentConsentList(String year) {
        String sqlQuery = environment.getProperty("CommonDao.getEligibleParentConsentList");
        NativeQuery hQuery = (NativeQuery) hibernateQuery(sqlQuery, ParentConsentDto.class)
                .setParameter("year", year);
        return hQuery.list().isEmpty() ? null : hQuery.list();
    }
}
