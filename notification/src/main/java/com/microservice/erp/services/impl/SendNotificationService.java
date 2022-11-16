package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.NoticeDto;
import com.microservice.erp.domain.entities.NoticeConfiguration;
import com.microservice.erp.domain.entities.SendNoticeInfo;
import com.microservice.erp.domain.repositories.INoticeConfigurationRepository;
import com.microservice.erp.domain.repositories.SendNoticeInfoRepository;
import com.microservice.erp.services.iServices.ISendNotificationService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SendNotificationService implements ISendNotificationService {
    private INoticeConfigurationRepository iNoticeConfigurationRepository;
    private SendNoticeInfoRepository sendNoticeInfoRepository;

    @Override
    public ResponseEntity<?> sendNotification(NoticeDto noticeDto) {

        //todo: logic to send notification
        //todo:send email and sms
        //todo:need to import mail sender codes form other services

        NoticeConfiguration noticeConfigurationDb = iNoticeConfigurationRepository.findById(noticeDto.getNoticeConfigurationId()).get();
        SendNoticeInfo sendNoticeInfo = new ModelMapper().map(noticeConfigurationDb, SendNoticeInfo.class);
        sendNoticeInfo.setYear(noticeDto.getYear());
        sendNoticeInfoRepository.save(sendNoticeInfo);
        return ResponseEntity.ok("Notification sent successfully.");
    }
}
