package com.microservice.erp.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.erp.domain.dto.MailSenderDto;
import com.microservice.erp.domain.dto.NoticeDto;
import com.microservice.erp.domain.dto.UserProfileDto;
import com.microservice.erp.domain.entities.NoticeConfiguration;
import com.microservice.erp.domain.entities.SendNoticeInfo;
import com.microservice.erp.domain.helper.MailSender;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.helper.SmsSender;
import com.microservice.erp.domain.repositories.INoticeConfigurationRepository;
import com.microservice.erp.domain.repositories.SendNoticeInfoRepository;
import com.microservice.erp.services.iServices.ISendNotificationService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class SendNotificationService implements ISendNotificationService {
    private INoticeConfigurationRepository iNoticeConfigurationRepository;
    private SendNoticeInfoRepository sendNoticeInfoRepository;
    private MailSenderService mailSenderService;

    @Override
    public ResponseEntity<?> checkNoticeAlreadySentOrNot(String year, Long noticeConfigurationId) {
        List<SendNoticeInfo> sendNoticeInfoDb = sendNoticeInfoRepository.findByNoticeConfigurationIdAndYear(noticeConfigurationId, year);
        if (sendNoticeInfoDb.size() > 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("Notification for the year " + year + " was already sent."));
        }
        return ResponseEntity.ok("");
    }

    public ResponseEntity<?> sendNotification(String authHeader, NoticeDto noticeDto) throws Exception {

        //get all eligible users by date and age
        NoticeConfiguration noticeConfigurationDb = iNoticeConfigurationRepository.findById(noticeDto.getNoticeConfigurationId()).get();
        SendNoticeInfo sendNoticeInfo = new SendNoticeInfo();
        sendNoticeInfo.setNoticeConfigurationId(noticeDto.getNoticeConfigurationId());
        sendNoticeInfo.setYear(noticeDto.getYear());
        sendNoticeInfo.setNoticeName(noticeConfigurationDb.getNoticeName());
        sendNoticeInfo.setNoticeBody(noticeConfigurationDb.getNoticeBody());
        sendNoticeInfo.setClassId(noticeConfigurationDb.getClassId());
        sendNoticeInfo.setAge(noticeConfigurationDb.getAge());
        sendNoticeInfo.setSendSms(noticeConfigurationDb.getSendSms());
        sendNoticeInfo.setSendEmail(noticeConfigurationDb.getSendEmail());
        sendNoticeInfoRepository.save(sendNoticeInfo);

        String paramDate = noticeDto.getYear() + "/12/31"; //converted to date 31st december and append selected year from UI
        Integer paramAge = noticeConfigurationDb.getAge();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<String> request = new HttpEntity<>(headers);
        String url = "http://localhost:81/api/user/profile/userProfile/getAllUsersEligibleForTraining?paramDate=" + paramDate + "&paramAge=" + paramAge;
        ResponseEntity<UserProfileDto[]> userDtoResponse = restTemplate.exchange(url, HttpMethod.GET, request, UserProfileDto[].class);
        ObjectMapper mapper = new ObjectMapper();

        for (UserProfileDto userProfileDto : Objects.requireNonNull(userDtoResponse.getBody())) {
            String smsBody = noticeConfigurationDb.getNoticeBody();
            String emailBody = "Dear " + userProfileDto.getFullName() + ", " + noticeConfigurationDb.getNoticeBody();
            String subject = noticeConfigurationDb.getNoticeName();

            MailSenderDto mailSenderDto = MailSenderDto.withId(
                    userProfileDto.getEmail(),
                    null,
                    null,
                    smsBody,
                    subject,
                    userProfileDto.getMobileNo());

            mailSenderService.sendSms(mapper.writeValueAsString(
                    mailSenderDto
            ),null);

            mailSenderDto.setMessageBody(emailBody);

            mailSenderService.sendEmail(mapper.writeValueAsString(
                    mailSenderDto
            ),null);
        }
        return ResponseEntity.ok("Notification sent successfully.");
    }
}
