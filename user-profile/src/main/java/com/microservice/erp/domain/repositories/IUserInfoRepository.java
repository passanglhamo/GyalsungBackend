package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Repository
public interface IUserInfoRepository extends JpaRepository<UserInfo, BigInteger> {

    @Override
    long count();

    UserInfo findByCid(String cid);

    UserInfo findByEmail(String searchKey);

    UserInfo findByUsername(String searchKey);

    @Query(value = "select * from sa_user u where date_part('year',age(:paramDate, u.dob)) >=:paramAge", nativeQuery = true)
    List<UserInfo> getAllUsersEligibleForTraining(Date paramDate, Integer paramAge);

    List<UserInfo> findAllBySignupUserOrderByFullNameAsc(char signupUser);
}
