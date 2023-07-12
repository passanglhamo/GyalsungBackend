package com.microservice.erp.services.iServices;

import com.microservice.erp.domain.dto.MedicalConfigurationBulkDto;
import com.microservice.erp.domain.entities.MedicalConfiguration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

public interface IMedicalConfigurationService {
    ResponseEntity<?> readFile(String authHeader,@Valid MedicalExcelCommand command);

    ResponseEntity<?> bulkSave(MedicalConfigurationBulkDto medicalConfigurationBulkDto);

    List<MedicalConfiguration> getAllMedicalConfigurationList();

    ResponseEntity<?> updateMedicalConfiguration(MedicalConfiguration medicalConfiguration);

    MedicalConfiguration save(MedicalConfiguration medicalConfiguration);

    @Getter
    @Setter
    class MedicalExcelCommand {
        private MultipartFile attachedFile;
    }
}
