package com.microservice.erp.domain.dao;


import com.microservice.erp.domain.dto.PermissionListDto;
import com.microservice.erp.domain.helper.BaseDao;
import org.hibernate.SQLQuery;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Repository
public class RoleWiseAccessPermissionDao extends BaseDao {
    private final Environment environment;

    public RoleWiseAccessPermissionDao(Environment environment) {
        this.environment = environment;
    }

    @Transactional
    public boolean getIsRoleMapped(BigInteger roleId) {
        String sqlQuery = environment.getProperty("CommonDao.getIsRoleMapped");
        NativeQuery hQuery = (NativeQuery) hibernateQuery(sqlQuery).setParameter("roleId", roleId);
        return hQuery.list().size() > 0;
    }

    @Transactional
    public List<PermissionListDto> getRoleMappedScreens(BigInteger roleId) {
        String sqlQuery = environment.getProperty("CommonDao.getRoleMappedScreens");
        NativeQuery hQuery = (NativeQuery) hibernateQuery(sqlQuery, PermissionListDto.class);
        hQuery.setParameter("roleId", roleId);
        return hQuery.list().isEmpty() ? null : hQuery.list();
    }

    @Transactional
    public List<PermissionListDto> getRoleUnmappedScreens() {
        String sqlQuery = environment.getProperty("CommonDao.getRoleUnmappedScreens");
        NativeQuery hQuery = (NativeQuery) hibernateQuery(sqlQuery, PermissionListDto.class);
        return hQuery.list().isEmpty() ? null : hQuery.list();
    }
}
