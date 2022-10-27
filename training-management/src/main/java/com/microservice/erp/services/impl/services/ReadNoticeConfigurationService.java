package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.entities.NoticeConfiguration;
import com.microservice.erp.domain.repository.INoticeConfigurationRepository;
import com.microservice.erp.services.iServices.IReadNoticeConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadNoticeConfigurationService implements IReadNoticeConfigurationService {

    private final INoticeConfigurationRepository repository;

    @Override
    public NoticeConfiguration getAllNoticeConfigurationById(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public List<NoticeConfiguration> getAllNoticeConfigurationList() {
        return repository.findAll();
    }
}
