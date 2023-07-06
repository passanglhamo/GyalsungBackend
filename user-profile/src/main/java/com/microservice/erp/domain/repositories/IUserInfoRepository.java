package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.*;

@Repository
public interface IUserInfoRepository extends JpaRepository<UserInfo, BigInteger> {

    @Override
    long count();

    Optional<UserInfo> findByCid(String cid);

    Optional<UserInfo> findByEmail(String searchKey);

    Optional<UserInfo> findByUsername(String searchKey);

    @Query(value = "select * from user_info u where date_part('year',age(:paramDate, u.dob)) >=:paramAge", nativeQuery = true)
    List<UserInfo> getAllUsersEligibleForTraining(Date paramDate, Integer paramAge);

    List<UserInfo> findAllBySignupUserOrderByFullNameAsc(char signupUser);

    @Query(value = "select * from user_info u where signup_user =:signupUser AND created_date <=:tillDate", nativeQuery = true)
    List<UserInfo> getAllUserTillDate(char signupUser, Date tillDate);

    List<UserInfo> findByCidIn(Set<String> cidNos);

    List<UserInfo> findAllByCidStartsWith(String cid);

    Optional<UserInfo> findByMobileNo(String mobileNo);

    UserInfo findFirstByOrderByUserIdDesc();

}
