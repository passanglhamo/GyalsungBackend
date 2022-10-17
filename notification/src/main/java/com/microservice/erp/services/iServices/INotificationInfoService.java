package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.repositories.INotificationInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Rajib Kumer Ghosh
 *
 */

public interface INotificationInfoService {
    void setINotificationInfoRepository(@Autowired INotificationInfoRepository repository);

    public String getHello();
}
