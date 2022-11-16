package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.NoticeConfiguration;

public interface ICreateNoticeConfigurationService {

    NoticeConfiguration saveNoticeConfiguration(NoticeConfiguration noticeConfiguration);
}
