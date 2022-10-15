package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Rajib Kumer Ghosh
 */

@Repository
public interface IUserInfoRepository extends JpaRepository<UserInfo, Long> {
    //List<UserInfo> findByName(String name);
    //long countByName(String name);

    @Override
    long count();

    @Override
    Page<UserInfo> findAll(Pageable pageable);

    UserInfo findByCid(String cid);

    UserInfo findByEmail(String searchKey);

    UserInfo findByUsername(String searchKey);
}
