package com.microservice.erp.services.impl.exemption;

import com.microservice.erp.domain.dto.feignClient.user.UserProfileDto;
import com.microservice.erp.domain.mapper.exemption.ExemptionMapper;
import com.microservice.erp.domain.repositories.IExemptionInfoRepository;
import com.microservice.erp.domain.helper.ApprovalStatus;
import com.microservice.erp.domain.helper.MailSender;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.helper.SmsSender;
import com.microservice.erp.services.iServices.exemption.ICreateExemptionService;
import com.microservice.erp.services.impl.common.HeaderToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CreateExemptionService implements ICreateExemptionService {
    private final IExemptionInfoRepository repository;
    private final ExemptionMapper mapper;
    //    private final SendEmailSms sendEmailSms;
    private final HeaderToken headerToken;

    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<?> saveExemption(HttpServletRequest request, CreateExemptionCommand command) throws Exception {
        String authTokenHeader = request.getHeader("Authorization");

        boolean exemptionInfoExist = repository.existsByUserIdAndStatusIn(command.getUserId(),
                Set.of(ApprovalStatus.PENDING.value(), ApprovalStatus.APPROVED.value()));

        if (exemptionInfoExist) {
            return new ResponseEntity<>("Exemption is already saved.", HttpStatus.ALREADY_REPORTED);
        }
        var exemption = repository.save(
                mapper.mapToEntity(
                        request, command
                )
        );

        repository.save(exemption);

        sendEmailAndSms(authTokenHeader, exemption.getUserId());

        return ResponseEntity.ok(new MessageResponse("An acknowledgement notifcation will be sent  to you as soon as you submit your  application.\" +\n" +
                "                    \"Your Deferment application will be  reviewed and the outcome  of the deferment wil be sent \" +\n" +
                "                    \" to you throught your email. If you  are not approved for deferment , you will have to complete the \" +\n" +
                "                    \" Gyalsung pre-enlistment procedure"));
    }

    private void sendEmailAndSms(String authTokenHeader, BigInteger userId) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpRequest = headerToken.tokenHeader(authTokenHeader);

        String userUrl = "http://localhost:81/api/user/profile/userProfile/getProfileInfo?userId=" + userId;
        ResponseEntity<UserProfileDto> userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, httpRequest, UserProfileDto.class);

        String subject = "Acknowledged for Exemption";

        String emailMessage = "Dear " + Objects.requireNonNull(userResponse.getBody()).getFullName() + ",\n" +
                "\n" +
                "This is to acknowledge the receipt of your exemption application. Your exemption application will be reviewed and the outcome of the exemption will be sent to you through your email within 10 days of the submission of your application. If you are not approved for exemption, you will have to complete the Gyalsung pre-enlistment procedure. \n";


        MailSender.sendMail(Objects.requireNonNull(userResponse.getBody()).getEmail(), null, null, emailMessage, subject);

        SmsSender.sendSms(Objects.requireNonNull(userResponse.getBody()).getMobileNo(), emailMessage);
    }
}
