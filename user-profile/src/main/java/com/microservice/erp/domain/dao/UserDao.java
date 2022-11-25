package com.microservice.erp.domain.dao;

import com.microservice.erp.domain.dto.UserProfileDto;
import com.microservice.erp.domain.entities.UserInfo;
import com.microservice.erp.domain.helper.BaseDao;
import org.hibernate.SQLQuery;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Repository
public class UserDao extends BaseDao {
    private final Environment environment;

    public UserDao(Environment environment) {
        this.environment = environment;
    }

    @Transactional
    public UserProfileDto checkUnderAge(BigInteger userId, Date paramDate) {
        String sqlQuery = environment.getProperty("CommonDao.checkUnderAge");
         try {
            return (UserProfileDto) entityManager.createNativeQuery(sqlQuery)
                    .setParameter("userId", userId)
                    .setParameter("paramDate", paramDate)
                    .unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(UserProfileDto.class))
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
