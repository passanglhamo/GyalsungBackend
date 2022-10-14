package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.UserInfo;
import com.microservice.erp.domain.repositories.IUserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Rajib Kumer Ghosh
 *
 */

public interface IUserInfoService {
    void setIUserInfoRepository(@Autowired IUserInfoRepository repository);
    UserInfo add(UserInfo aUserInfo);
    UserInfo update(UserInfo aUserInfo);
    boolean remove(Long userid);
    Long totalCount();
    UserInfo findByUserID(Long userid);
    List<UserInfo> findAllByUserID(List<Long> userid);
    List<UserInfo> findAll(Integer page, Integer size);
}
