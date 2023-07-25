package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.MedicalConfigurationBulkDto;
import com.microservice.erp.domain.dto.MedicalConfigurationDto;
import com.microservice.erp.domain.entities.MedicalConfiguration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public interface IMedicalConfigurationService {
    ResponseEntity<?> readFile(String authHeader,@Valid MedicalExcelCommand command);

    ResponseEntity<?> bulkSave(MedicalConfigurationBulkDto medicalConfigurationBulkDto);

    List<MedicalConfiguration> getAllMedicalConfigurationList();

    ResponseEntity<?> updateMedicalConfiguration(MedicalConfiguration medicalConfiguration);

    ResponseEntity<?> save(MedicalConfiguration medicalConfiguration);

    MedicalConfigurationDto getHospitalBookingDetailByBookingId(String authHeader, Integer hospitalId,
                                                                       Date appointmentDate);

    List<MedicalConfigurationDto> getAllAppointmentDateByHospitalId(Integer hospitalId);

    ResponseEntity<?> removeById(BigInteger id);


    @Getter
    @Setter
    class MedicalExcelCommand {
        private MultipartFile attachedFile;
    }
}
