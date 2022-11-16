package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.NoticeDto;
import com.microservice.erp.domain.dto.UserProfileDto;
import com.microservice.erp.domain.entities.NoticeConfiguration;
import com.microservice.erp.domain.entities.SendNoticeInfo;
import com.microservice.erp.domain.repositories.INoticeConfigurationRepository;
import com.microservice.erp.domain.repositories.SendNoticeInfoRepository;
import com.microservice.erp.services.iServices.ISendNotificationService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;

@Service
@AllArgsConstructor
public class SendNotificationService implements ISendNotificationService {
    private INoticeConfigurationRepository iNoticeConfigurationRepository;
    private SendNoticeInfoRepository sendNoticeInfoRepository;

    @Override
    public ResponseEntity<?> sendNotification(String authHeader, NoticeDto noticeDto) {

        //todo: logic to send notification
        //get all eligible users by date and age
        NoticeConfiguration noticeConfigurationDb = iNoticeConfigurationRepository.findById(noticeDto.getNoticeConfigurationId()).get();
        String paramDate = noticeDto.getYear() + "/12/31"; //converted to date 31st december and append selected year from UI
        Integer paramAge = noticeConfigurationDb.getAge();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<String> request = new HttpEntity<>(headers);
        String url = "http://localhost:81/api/user/profile/userProfile/getAllUsersEligibleForTraining?paramDate=" + paramDate + "&paramAge=" + paramAge;
        ResponseEntity<UserProfileDto[]> userDtoResponse = restTemplate.exchange(url, HttpMethod.GET, request, UserProfileDto[].class);

        for (UserProfileDto userProfileDto : Objects.requireNonNull(userDtoResponse.getBody())) {
            //todo: send email and sms
        }

        SendNoticeInfo sendNoticeInfo = new ModelMapper().map(noticeConfigurationDb, SendNoticeInfo.class);
        sendNoticeInfo.setYear(noticeDto.getYear());
        sendNoticeInfoRepository.save(sendNoticeInfo);
        return ResponseEntity.ok("Notification sent successfully.");
    }
}
