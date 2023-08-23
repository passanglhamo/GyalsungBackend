package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.ApplicationProperties;
import com.microservice.erp.domain.dto.ReasonDto;
import com.microservice.erp.domain.entities.DefermentInfo;
import com.microservice.erp.domain.entities.ExemptionInfo;
import com.microservice.erp.domain.repositories.IDefermentInfoRepository;
import com.microservice.erp.domain.repositories.IExemptionInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CaseNumberGenerator {
    private final IDefermentInfoRepository repository;
    private final IExemptionInfoRepository exemptionInfoRepository;
    @Autowired
    @Qualifier("trainingManagementTemplate")
    RestTemplate restTemplate;

    public String caseNumberGenerator(String authHeader, BigInteger reasonId,Character deferExempt) {

        String initialRandomNo= "0001";
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<String> request = new HttpEntity<>(headers);
        String urlTraining = properties.getReasonById() + reasonId;
        ResponseEntity<ReasonDto> responseTraining = restTemplate.exchange(urlTraining, HttpMethod.GET, request, ReasonDto.class);

        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyMMdd"));
        String caseNumber = "";
        if(deferExempt.equals('D')){
            DefermentInfo defermentInfo = repository.getLatestDefermentByReasonIdAndApplicationDate(reasonId,new Date());
            if(Objects.isNull(defermentInfo)){
                return formattedDate+"/"+deferExempt+Objects.requireNonNull(responseTraining.getBody()).getCode()+"/"+initialRandomNo;
            }
            caseNumber = defermentInfo.getCaseNumber();

        }
        if(deferExempt.equals('E')){
            ExemptionInfo exemptionInfo = exemptionInfoRepository.findByReasonIdAndApplicationDateOrderByExemptionIdDesc(reasonId,new Date());
            if(Objects.isNull(exemptionInfo)){
                return formattedDate+"/"+deferExempt+Objects.requireNonNull(responseTraining.getBody()).getCode()+"/"+initialRandomNo;
            }
            caseNumber = exemptionInfo.getCaseNumber();

        }

        String desiredPart = caseNumber.substring(caseNumber.lastIndexOf("/") + 1);
        String value =String.format("%04d", Long.parseLong(desiredPart) + 1);



        return formattedDate+"/"+deferExempt+Objects.requireNonNull(responseTraining.getBody()).getCode()+"/"+value;
    }
}
