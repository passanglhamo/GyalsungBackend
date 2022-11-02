package com.microservice.erp.services.impl.exemption;

import com.microservice.erp.domain.dto.feignClient.user.UserProfileDto;
import com.microservice.erp.domain.mapper.exemption.ExemptionMapper;
import com.microservice.erp.domain.repositories.IExemptionInfoRepository;
import com.microservice.erp.services.helper.ApprovalStatus;
import com.microservice.erp.services.helper.MessageResponse;
import com.microservice.erp.services.iServices.exemption.ICreateExemptionService;
import com.microservice.erp.services.impl.common.HeaderToken;
import com.microservice.erp.services.impl.deferment.SendEmailSms;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CreateExemptionService implements ICreateExemptionService {
    private final IExemptionInfoRepository repository;
    private final ExemptionMapper mapper;
    private final SendEmailSms sendEmailSms;
    private final HeaderToken headerToken;

    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<?> saveExemption(HttpServletRequest request, CreateExemptionCommand command) throws IOException {
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

        //sendEmailAndSms(authTokenHeader, exemption.getUserId());

        return ResponseEntity.ok(new MessageResponse("An acknowledgement notifcation will be sent  to you as soon as you submit your  application.\" +\n" +
                "                    \"Your Deferment application will be  reviewed and the outcome  of the deferment wil be sent \" +\n" +
                "                    \" to you throught your email. If you  are not approved for deferment , you will have to complete the \" +\n" +
                "                    \" Gyalsung pre-enlistment procedure"));
    }

    private void sendEmailAndSms(String authTokenHeader, Long userId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpRequest = headerToken.tokenHeader(authTokenHeader);

        String userUrl = "http://localhost:81/api/user/profile/userProfile/getProfileInfo?userId=" + userId;
        ResponseEntity<UserProfileDto> userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, httpRequest, UserProfileDto.class);

        String emailmessage = "Dear, The verification code for Gyalsung system is";

        sendEmailSms.sendEmail(Objects.requireNonNull(userResponse.getBody()).getEmail(), emailmessage);

        String message = "Your OTP for Gyalsung Registration is ";

        restTemplate.exchange("http://172.30.16.213/g2csms/push.php?to=" + Objects.requireNonNull(userResponse.getBody()).getMobileNo() + "&msg=" + message, HttpMethod.GET, null, String.class);
    }
}
