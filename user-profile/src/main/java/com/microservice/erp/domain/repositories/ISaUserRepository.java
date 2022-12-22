package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.SaUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Repository
public interface ISaUserRepository extends JpaRepository<SaUser, BigInteger> {

    @Override
    long count();

    SaUser findByCid(String cid);

    SaUser findByEmail(String searchKey);

    SaUser findByUsername(String searchKey);

    @Query(value = "select * from sa_user u where date_part('year',age(:paramDate, u.dob)) >=:paramAge", nativeQuery = true)
    List<SaUser> getAllUsersEligibleForTraining(Date paramDate, Integer paramAge);

    List<SaUser> findAllBySignupUserOrderByFullNameAsc(char signupUser);
}
