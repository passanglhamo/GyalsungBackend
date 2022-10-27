package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.entities.NoticeConfiguration;
import com.microservice.erp.domain.repository.INoticeConfigurationRepository;
import com.microservice.erp.services.iServices.ICreateNoticeConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateNoticeConfigurationService implements ICreateNoticeConfigurationService {
    private final INoticeConfigurationRepository repository;

    @Override
    public NoticeConfiguration saveNoticeConfiguration(NoticeConfiguration noticeConfiguration) {
        return repository.save(noticeConfiguration);
    }
}
