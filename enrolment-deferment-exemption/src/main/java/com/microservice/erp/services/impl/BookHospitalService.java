package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dao.BookHospitalDao;
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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BookHospitalService implements IBookHospitalService {
    private final BookHospitalDao bookHospitalDao;
    private final IHospitalBookingRepository iHospitalBookingRepository;
    private final UserInformationService userInformationService;

    @Autowired
    @Qualifier("trainingManagementTemplate")
    RestTemplate restTemplate;

    public BookHospitalService(BookHospitalDao bookHospitalDao, IHospitalBookingRepository iHospitalBookingRepository, UserInformationService userInformationService) {
        this.bookHospitalDao = bookHospitalDao;
        this.iHospitalBookingRepository = iHospitalBookingRepository;
        this.userInformationService = userInformationService;
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
    public ResponseEntity<?> getPreviousBookingDetailByUserId(String authHeader, BookHospitalDto bookHospitalDto) {
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

    @Override
    public ResponseEntity<?> getAllBookingByHospitalIdAndYear(String authHeader, BigInteger year, Integer hospitalId) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);

        List<BookHospitalDto> bookHospitalDtos = bookHospitalDao.getAllBookingByYearAndHospitalId(year, hospitalId);
        List<BigInteger> userIds = bookHospitalDtos
                .stream()
                .map(BookHospitalDto::getUser_id)
                .collect(Collectors.toList());
        List<UserProfileDto> userProfileDtos = userInformationService.getUserInformationByListOfIds(userIds, authHeader);

        List<BookHospitalListDto> bookHospitalListDtos = new ArrayList<>();
        bookHospitalDtos.forEach(item -> {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", authHeader);
            HttpEntity<String> request = new HttpEntity<>(headers);

            UserProfileDto userProfileDto = userProfileDtos
                    .stream()
                    .filter(user -> item.getUser_id().equals(user.getId()))
                    .findAny()
                    .orElse(null);

            BookHospitalListDto bookHospitalListDto = new BookHospitalListDto();
            bookHospitalListDto.setUser_id(item.getUser_id());
            bookHospitalListDto.setFull_name(userProfileDto.getFullName());
            bookHospitalListDto.setCid(userProfileDto.getCid());
            bookHospitalListDto.setGender(userProfileDto.getGender());
            bookHospitalListDto.setScreening_date(item.getScreening_date());

            bookHospitalListDto.setHospital_booking_id(item.getHospital_booking_id());


            String hospitalUrl = properties.getTrainingHospitalById() + hospitalId;
            ResponseEntity<HospitalDto> hospitalDtoResponseEntity = restTemplate.exchange(hospitalUrl, HttpMethod.GET, request, HospitalDto.class);
            bookHospitalListDto.setHospital_name(Objects.requireNonNull(hospitalDtoResponseEntity.getBody()).getHospitalName());


            bookHospitalListDtos.add(bookHospitalListDto);
        });
        return ResponseEntity.ok(bookHospitalListDtos);
    }

}

