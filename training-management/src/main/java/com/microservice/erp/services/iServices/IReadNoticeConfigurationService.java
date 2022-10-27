package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.entities.NoticeConfiguration;

import java.util.List;

public interface IReadNoticeConfigurationService {

    NoticeConfiguration getAllNoticeConfigurationById(Long id);

    List<NoticeConfiguration> getAllNoticeConfigurationList();
}
