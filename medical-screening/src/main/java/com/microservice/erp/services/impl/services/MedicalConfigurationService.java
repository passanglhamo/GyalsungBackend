package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.dto.*;
import com.microservice.erp.domain.entities.HospitalBookingDetail;
import com.microservice.erp.domain.entities.MedicalConfiguration;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.mapper.MedicalConfigurationMapper;
import com.microservice.erp.domain.repositories.IHospitalBookingDetailsRepository;
import com.microservice.erp.domain.repositories.IMedicalConfigurationRepository;
import com.microservice.erp.services.iServices.IMedicalConfigurationService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MedicalConfigurationService implements IMedicalConfigurationService {

    @Autowired
    @Qualifier("trainingManagementTemplate")
    RestTemplate restTemplate;

    private final IMedicalConfigurationRepository medicalConfigurationRepository;
    private final IHospitalBookingDetailsRepository hospitalBookingDetailsRepository;
    private final MedicalConfigurationMapper medicalConfigurationMapper;

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

    @Override
    public ResponseEntity<?> bulkSave(MedicalConfigurationBulkDto medicalConfigurationBulkDto) {
        medicalConfigurationBulkDto.getMedicalConfigurationDtos().forEach(
                medicalConfigurationDto -> {
                    Optional<MedicalConfiguration> medicalConfiguration = medicalConfigurationRepository.findByHospitalIdAndAppointmentDate(medicalConfigurationDto.getHospitalId(),
                            medicalConfigurationDto.getAppointmentDate());
                    if (medicalConfiguration.isPresent()) {
                        update(medicalConfiguration.get().getId(), medicalConfigurationDto.getAmSlots(),
                                medicalConfigurationDto.getPmSlots());
                    } else {
                        save(medicalConfigurationDto);
                    }
                }
        );
        return ResponseEntity.ok("Data saved successfully.");
    }

    @Override
    public List<MedicalConfiguration> getAllMedicalConfigurationList() {
        return medicalConfigurationRepository.findAll();
    }

    @Override
    public ResponseEntity<?> updateMedicalConfiguration(MedicalConfiguration medicalConfiguration) {
        if(medicalConfigurationRepository.existsByHospitalIdAndAppointmentDateAndIdNot(medicalConfiguration.getHospitalId(),
                medicalConfiguration.getAppointmentDate(), medicalConfiguration.getId())){
            return new ResponseEntity<>("Selected hospital and appointment date is already added.", HttpStatus.ALREADY_REPORTED);
        }
        if(hospitalBookingDetailsRepository.findAllByHospitalBookingId(medicalConfiguration.getId()).size()!=0){
            return new ResponseEntity<>("Due to the presence of booked users, the information cannot be updated at this time.", HttpStatus.ALREADY_REPORTED);

        }
        update(medicalConfiguration.getId(), medicalConfiguration.getAmSlots(), medicalConfiguration.getPmSlots());

        return ResponseEntity.ok("Data updated successfully.");
    }

    @Override
    public ResponseEntity<?> save(MedicalConfiguration medicalConfiguration) {
        if(medicalConfigurationRepository.findByHospitalIdAndAppointmentDate(medicalConfiguration.getHospitalId(),
                medicalConfiguration.getAppointmentDate()).isPresent()){
                return new ResponseEntity<>("Selected hospital and appointment date is already added.", HttpStatus.ALREADY_REPORTED);
        }

        medicalConfigurationRepository.save(medicalConfiguration);

        return ResponseEntity.ok("Medical Configuration saved successfully.");

    }

    private void save(MedicalConfigurationDto medicalConfigurationDto) {
        MedicalConfiguration medicalConfiguration = new MedicalConfiguration();
        medicalConfiguration.setHospitalId(medicalConfigurationDto.getHospitalId());
        medicalConfiguration.setAppointmentDate(medicalConfigurationDto.getAppointmentDate());
        medicalConfiguration.setAmSlots(medicalConfigurationDto.getAmSlots());
        medicalConfiguration.setPmSlots(medicalConfigurationDto.getPmSlots());
        medicalConfiguration.setStatus('A');
        medicalConfigurationRepository.save(medicalConfiguration);
    }

    private void update(BigInteger id, Integer amSlots, Integer pmSlots) {
        medicalConfigurationRepository.findById(id).ifPresent(d -> {
            d.setAmSlots(amSlots);
            d.setPmSlots(pmSlots);
            medicalConfigurationRepository.save(d);
        });
    }

    @Override
    public MedicalConfigurationDto getHospitalBookingDetailByBookingId(String authHeader, Integer hospitalId,
                                                                       Date appointmentDate) {
        MedicalConfiguration medicalConfiguration = medicalConfigurationRepository.findByHospitalIdAndAppointmentDate(hospitalId, appointmentDate).get();

        List<HospitalBookingDetail> getBookedUserAm = hospitalBookingDetailsRepository.findAllByHospitalBookingIdAndAmPm(
                medicalConfiguration.getId(), 'A'
        );
        Integer countAmBooked = Objects.isNull(getBookedUserAm) ? 0 : getBookedUserAm.size();

        List<HospitalBookingDetail> getBookedUserPm = hospitalBookingDetailsRepository.findAllByHospitalBookingIdAndAmPm(
                medicalConfiguration.getId(), 'P'
        );
        Integer countPmBooked = Objects.isNull(getBookedUserPm) ? 0 : getBookedUserPm.size();

        return MedicalConfigurationDto.withId(
                medicalConfiguration.getId(),
                medicalConfiguration.getHospitalId(),
                medicalConfiguration.getAppointmentDate(),
                medicalConfiguration.getAmSlots(),
                (medicalConfiguration.getAmSlots() - countAmBooked),
                medicalConfiguration.getPmSlots(),
                (medicalConfiguration.getPmSlots() - countPmBooked),
                medicalConfiguration.getStatus(),
                null
        );
    }

    @Override
    public List<MedicalConfigurationDto> getAllAppointmentDateByHospitalId(Integer hospitalId) {
        return medicalConfigurationRepository.findAllByHospitalId(hospitalId)
                .stream()
                .map(medicalConfigurationMapper::mapToDomain)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public ResponseEntity<?> removeById(BigInteger id) {
        if(hospitalBookingDetailsRepository.findAllByHospitalBookingId(id).size()!=0){
            return new ResponseEntity<>("Due to the presence of booked users, the information cannot be deleted at this time.", HttpStatus.ALREADY_REPORTED);

        }
        medicalConfigurationRepository.deleteById(id);
        return  ResponseEntity.ok("Deleted successfully.");
    }


}
