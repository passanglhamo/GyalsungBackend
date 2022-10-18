package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.repository.IEnlistmentScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Rajib Kumer Ghosh
 *
 */

public interface IEnlistmentScheduleService {
    void setIEnlistmentScheduleRepository(@Autowired IEnlistmentScheduleRepository repository);

    public String getHello();
}
