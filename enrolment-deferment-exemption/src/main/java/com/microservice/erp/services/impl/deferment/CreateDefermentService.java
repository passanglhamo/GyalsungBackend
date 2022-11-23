package com.microservice.erp.services.impl.deferment;

import com.microservice.erp.domain.dto.feignClient.user.UserProfileDto;
import com.microservice.erp.domain.mapper.deferment.DefermentMapper;
import com.microservice.erp.domain.repositories.IDefermentInfoRepository;
import com.microservice.erp.domain.helper.ApprovalStatus;
import com.microservice.erp.domain.helper.MailSender;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.helper.SmsSender;
import com.microservice.erp.services.iServices.deferment.ICreateDefermentService;
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
public class CreateDefermentService implements ICreateDefermentService {

    private final IDefermentInfoRepository repository;
    private final DefermentMapper mapper;
    private final HeaderToken headerToken;
    Integer fileLength = 5;


    @Transactional
    public ResponseEntity<?> saveDeferment(HttpServletRequest request, CreateDefermentCommand command) {
        String authTokenHeader = request.getHeader("Authorization");
        boolean defermentInfoExist = repository.existsByUserIdAndStatusInAndToDateGreaterThanEqual(command.getUserId(),
                Set.of(ApprovalStatus.PENDING.value(), ApprovalStatus.APPROVED.value()), command.getToDate());


        if (defermentInfoExist) {
            return new ResponseEntity<>("Deferment is already saved.", HttpStatus.ALREADY_REPORTED);
        }


        if (!Objects.isNull(command.getProofDocuments())) {
            if (command.getProofDocuments().length > fileLength) {
                return new ResponseEntity<>("You can upload maximum of 5 files.", HttpStatus.ALREADY_REPORTED);
            }
        }

        var deferment = repository.save(
                mapper.mapToEntity(
                        request, command
                )
        );

        repository.save(deferment);

        try {
            sendEmailAndSms(authTokenHeader, deferment.getUserId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(new MessageResponse("Deferment is successfully saved"));
    }

    private void sendEmailAndSms(String authTokenHeader, BigInteger userId) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> httpRequest = headerToken.tokenHeader(authTokenHeader);

        String userUrl = "http://localhost:81/api/user/profile/userProfile/getProfileInfo?userId=" + userId;
        ResponseEntity<UserProfileDto> userResponse = restTemplate.exchange(userUrl, HttpMethod.GET, httpRequest, UserProfileDto.class);
        String subject = "Acknowledged for Deferment";

        String emailMessage = "Dear " + Objects.requireNonNull(userResponse.getBody()).getFullName() + ",\n" +
                "\n" +
                "This is to acknowledge the receipt of your deferment application. Your deferment application will be reviewed and the outcome of the deferment will be sent to you through your email within 10 days of the submission of your application. If you are not approved for deferment, you will have to complete the Gyalsung pre-enlistment procedure. \n";

        MailSender.sendMail(Objects.requireNonNull(userResponse.getBody()).getEmail(), null, null, emailMessage, subject);


        SmsSender.sendSms(Objects.requireNonNull(userResponse.getBody()).getMobileNo(), emailMessage);

    }


}
