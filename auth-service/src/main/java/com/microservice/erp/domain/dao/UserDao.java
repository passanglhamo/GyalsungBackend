package com.microservice.erp.domain.dao;

import com.microservice.erp.domain.helper.BaseDao;
import org.hibernate.query.NativeQuery;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigInteger;

@Repository
public class UserDao extends BaseDao {
    private final Environment environment;

    public UserDao(Environment environment) {
        this.environment = environment;
    }

//    @Transactional
//    public UserProfileDto checkUnderAge(BigInteger userId, Date paramDate) {
//        String sqlQuery = environment.getProperty("CommonDao.checkUnderAge");
//        try {
//            return (UserProfileDto) entityManager.createNativeQuery(sqlQuery)
//                    .setParameter("userId", userId)
//                    .setParameter("paramDate", paramDate)
//                    .unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(UserProfileDto.class))
//                    .getSingleResult();
//        } catch (NoResultException ex) {
//            return null;
//        }
//    }

    @Transactional
    public String isEmailAlreadyInUse(String email, BigInteger userId) {
        String sqlQuery = environment.getProperty("CommonDao.isEmailAlreadyInUse");
        NativeQuery hQuery = (NativeQuery) hibernateQuery(sqlQuery)
                .setParameter("email", email)
                .setParameter("userId", userId);
        return hQuery.list().isEmpty() ? null : (String) hQuery.list().get(0);
    }
}
