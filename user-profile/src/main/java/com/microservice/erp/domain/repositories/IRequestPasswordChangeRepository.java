package com.microservice.erp.domain.repositories;

 import com.microservice.erp.domain.entities.PwChangeRequest;
 import org.springframework.data.jpa.repository.JpaRepository;

 import java.math.BigInteger;

public interface IRequestPasswordChangeRepository extends JpaRepository<PwChangeRequest, String> {
    PwChangeRequest findByRequestId(BigInteger requestId);

    PwChangeRequest findByRequestIdAndEmail(BigInteger requestId, String email);
}
