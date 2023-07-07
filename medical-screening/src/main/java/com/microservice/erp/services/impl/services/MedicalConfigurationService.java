package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.dto.*;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.services.iServices.IMedicalConfigurationService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class MedicalConfigurationService implements IMedicalConfigurationService {

    @Autowired
    @Qualifier("trainingManagementTemplate")
    RestTemplate restTemplate;

    @Override
    public ResponseEntity<?> readFile(String authHeader, MedicalExcelCommand command) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<String> request = new HttpEntity<>(headers);
        MultipartFile attachedFile = command.getAttachedFile();
        List<MedicalConfigurationExcelDto> medicalConfigurationExList = new ArrayList<>();
        List<MedicalConfigurationDto> medicalConfigurationList = new ArrayList<>();

        try (Reader reader = new BufferedReader(new InputStreamReader(attachedFile.getInputStream()))) {
            CsvToBean csvToBean = new CsvToBeanBuilder(reader).withType(MedicalConfigurationExcelDto.class).withIgnoreLeadingWhiteSpace(true).build();
            medicalConfigurationExList = csvToBean.parse();
        } catch (Exception ex) {
            ResponseEntity.badRequest().body(new MessageResponse("An error occurred while processing the CSV file."));
        }

        final BigInteger[] excelId = {BigInteger.ZERO};
        medicalConfigurationExList.forEach(medical -> {
            String hospitalUrl = properties.getHospitalByName() + medical.getHospitalName();
            ResponseEntity<HospitalDto> hospitalDtoResponse = restTemplate.exchange(hospitalUrl, HttpMethod.GET, request, HospitalDto.class);
            if (!Objects.isNull(hospitalDtoResponse.getBody())) {
                MedicalConfigurationDto medicalConfigurationDto = new MedicalConfigurationDto();
                excelId[0] = excelId[0].add(BigInteger.ONE);
                medicalConfigurationDto.setId(excelId[0]);
                medicalConfigurationDto.setHospitalId(hospitalDtoResponse.getBody().getHospitalId());
                medicalConfigurationDto.setAmSlots(medical.getAmSlots());
                medicalConfigurationDto.setPmSlots(medical.getPmSlots());
                medicalConfigurationDto.setHospitalName(medical.getHospitalName());
                medicalConfigurationDto.setAppointmentDate(medical.getAppointmentDate());
                medicalConfigurationDto.setHospitalName(medical.getHospitalName());
                medicalConfigurationList.add(medicalConfigurationDto);
            }
        });

        return ResponseEntity.ok(medicalConfigurationList);
    }
}
