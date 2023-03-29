package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.*;
import com.microservice.erp.domain.entities.HospitalBooking;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.repositories.IHospitalBookingRepository;
import com.microservice.erp.services.iServices.IBookHospitalService;
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

import java.util.Objects;

@Service
public class BookHospitalService implements IBookHospitalService {
    private final IHospitalBookingRepository iHospitalBookingRepository;

    @Autowired
    @Qualifier("trainingManagementTemplate")
    RestTemplate restTemplate;

    public BookHospitalService(IHospitalBookingRepository iHospitalBookingRepository) {
        this.iHospitalBookingRepository = iHospitalBookingRepository;
    }

    @Override
    public ResponseEntity<?> getHospitalBookingByUserId(BookHospitalDto bookHospitalDto) {
        HospitalBooking hospitalBookingDb = iHospitalBookingRepository.findByUserId(bookHospitalDto.getUserId());
        if (hospitalBookingDb != null) {
            return ResponseEntity.ok(hospitalBookingDb);
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Data not found."));
        }
    }

    @Override
    public ResponseEntity<?> getPreviousBookingDetailByUserId(String authHeader, BookHospitalDto bookHospitalDto ) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);

        HospitalBooking hospitalBookingDb = iHospitalBookingRepository.findByUserId(bookHospitalDto.getUserId());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authHeader);
        HttpEntity<String> request = new HttpEntity<>(headers);

        String hospitalUrl = properties.getTrainingHospitalById() + hospitalBookingDb.getHospitalId();
        ResponseEntity<HospitalDto> hospitalDtoResponse = restTemplate.exchange(hospitalUrl, HttpMethod.GET, request, HospitalDto.class);
        String hospitalName = Objects.requireNonNull(hospitalDtoResponse.getBody()).getHospitalName();

        String dzongkhagUrl = properties.getTrainingMappedHospitalById() + hospitalBookingDb.getHospitalId();
        ResponseEntity<DzongkhagDto> dzongkhagDtoResponse = restTemplate.exchange(dzongkhagUrl, HttpMethod.GET, request, DzongkhagDto.class);
        String dzongkhagName = Objects.requireNonNull(dzongkhagDtoResponse.getBody()).getDzongkhagName();


        BookHospitalDto bookHospitalDtoResponse = new BookHospitalDto();
        bookHospitalDtoResponse.setHospitalName(hospitalName);
        bookHospitalDtoResponse.setDzongkhagName(dzongkhagName);
        bookHospitalDtoResponse.setScreeningDate(hospitalBookingDb.getScreeningDate());

        return ResponseEntity.ok(bookHospitalDtoResponse);
    }

    @Override
    public ResponseEntity<?> bookHospital(String authHeader, BookHospitalDto bookHospitalDto) {
        //check already booked or not, delete if already booked and save as new
        HospitalBooking hospitalBookingDb = iHospitalBookingRepository.findByUserId(bookHospitalDto.getUserId());
        if (hospitalBookingDb != null) {
            iHospitalBookingRepository.deleteById(bookHospitalDto.getHospitalBookingId());
        }

        HospitalBooking hospitalBooking = new HospitalBooking();
        hospitalBooking.setDzongkhagId(bookHospitalDto.getDzongkhagId());
        hospitalBooking.setHospitalId(bookHospitalDto.getHospitalId());
        hospitalBooking.setUserId(bookHospitalDto.getUserId());
        hospitalBooking.setScreeningDate(bookHospitalDto.getScreeningDate());
        iHospitalBookingRepository.save(hospitalBooking);
        return ResponseEntity.ok(new MessageResponse("Hospital booked successfully."));
    }
}

