package com.microservice.erp.domain.dao;

import com.microservice.erp.domain.dto.TestDto;
import com.microservice.erp.domain.helper.BaseDao;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
 public class MedicalBookingListDao extends  BaseDao{

    private final Environment environment;

    public MedicalBookingListDao(Environment environment) {
        this.environment = environment;
    }

    @Transactional
    public List<TestDto> testMethod() {
        String sql = environment.getProperty("CommonDao.getAllCompanies");
        List<TestDto> testDtos = entityManager.createNativeQuery(sql)
                .unwrap(SQLQuery.class)
                .setResultTransformer(Transformers.aliasToBean(TestDto.class)).getResultList();
        return testDtos;
    }
}
