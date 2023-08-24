package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.DefermentFileInfo;
import com.microservice.erp.domain.entities.DefermentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

/**
 * @author Rajib Kumer Ghosh
 */

@Repository
public interface IDefermentFileInfoRepository extends JpaRepository<DefermentFileInfo, BigInteger> {

    List<DefermentFileInfo> findAllByDeferment(BigInteger defermentId);

    DefermentFileInfo findFirstByOrderByDefermentFileIdDesc();

    Optional<DefermentFileInfo> findAllByDefermentFileId(BigInteger defermentFileId);
}
