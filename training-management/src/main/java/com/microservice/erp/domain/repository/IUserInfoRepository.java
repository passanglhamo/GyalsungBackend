//package com.microservice.erp.domain.repository;
//
//import com.microservice.erp.domain.entities.UserInfo;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
///**
// * @author Rajib Kumer Ghosh
// *
// */
//
//@Repository
//public interface IUserInfoRepository extends JpaRepository<UserInfo, Integer> {
//    List<UserInfo> findByName(String name);
//    long countByName(String name);
//
//    @Override
//    long count();
//
//    @Override
//    Page<UserInfo> findAll(Pageable pageable);
//}
