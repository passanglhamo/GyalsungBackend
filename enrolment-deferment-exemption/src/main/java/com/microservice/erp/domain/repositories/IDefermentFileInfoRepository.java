package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.DefermentFileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Rajib Kumer Ghosh
 */

@Repository
public interface IDefermentFileInfoRepository extends JpaRepository<DefermentFileInfo, Long> {

    List<DefermentFileInfo> findAllByDefermentId(Long defermentId);

}