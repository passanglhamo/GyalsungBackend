package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.Statement;
import com.microservice.erp.domain.models.Action;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface IStatementRepository extends JpaRepository<Statement, BigInteger> {
    List<Statement> findAllByAction(Action action);

}
