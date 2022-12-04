package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.SaUser;
import com.microservice.erp.domain.repositories.ISaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Rajib Kumer Ghosh
 *
 */

public interface IUserInfoService {
    void setIUserInfoRepository(@Autowired ISaUserRepository repository);
    SaUser add(SaUser aSaUser);
    SaUser update(SaUser aSaUser);
    boolean remove(Long userid);
    Long totalCount();
    SaUser findByUserID(Long userid);
    List<SaUser> findAllByUserID(List<Long> userid);
    List<SaUser> findAll(Integer page, Integer size);
}
