package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, BigInteger> {
    Optional<User> findByUsername(String username);
    Optional<User> findByCid(String username);
    Optional<User> findByEmail(String username);

    Optional<User> findByUserId(BigInteger userId);
}
