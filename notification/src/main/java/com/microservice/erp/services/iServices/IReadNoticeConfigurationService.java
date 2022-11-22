package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.NoticeConfiguration;

import java.math.BigInteger;
import java.util.List;

public interface IReadNoticeConfigurationService {

    NoticeConfiguration getAllNoticeConfigurationById(BigInteger id);

    List<NoticeConfiguration> getAllNoticeConfigurationList();
}
