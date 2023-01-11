package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.AutomaticExemptionDto;
import com.microservice.erp.domain.entities.AutoExemptionUploadedFile;
import com.microservice.erp.domain.entities.AutomaticExemption;
import com.microservice.erp.domain.helper.FileUploadDTO;
import com.microservice.erp.domain.helper.FileUploadToExternalLocation;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.helper.ResponseMessage;
import com.microservice.erp.domain.repositories.IAutoExemptionUploadedFileRepository;
import com.microservice.erp.services.iServices.IAutomaticExemptionService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AutomaticExemptionService implements IAutomaticExemptionService {
    private final IAutoExemptionUploadedFileRepository iAutoExemptionUploadedFileRepository;

    public AutomaticExemptionService(IAutoExemptionUploadedFileRepository iAutoExemptionUploadedFileRepository) {
        this.iAutoExemptionUploadedFileRepository = iAutoExemptionUploadedFileRepository;
    }

    @Override
    public ResponseEntity<?> readFile(AutomaticExemptionDto automaticExemptionDto) {
        MultipartFile attachedFile = automaticExemptionDto.getAttachedFile();
        List<AutomaticExemption> automaticExemptionList = new ArrayList<>();
        try (Reader reader = new BufferedReader(new InputStreamReader(attachedFile.getInputStream()))) {
            CsvToBean csvToBean = new CsvToBeanBuilder(reader).withType(AutomaticExemption.class).withIgnoreLeadingWhiteSpace(true).build();
            automaticExemptionList = csvToBean.parse();
        } catch (Exception ex) {
            ResponseEntity.badRequest().body(new MessageResponse("An error occurred while processing the CSV file."));
        }
        return ResponseEntity.ok(automaticExemptionList);
    }

    @Override
    public ResponseEntity<?> uploadFile(HttpServletRequest request, AutomaticExemptionDto automaticExemptionDto) throws IOException {
        MultipartFile attachedFile = automaticExemptionDto.getAttachedFile();
        try (Reader reader = new BufferedReader(new InputStreamReader(attachedFile.getInputStream()))) {
            // create csv bean reader
            CsvToBean csvToBean = new CsvToBeanBuilder(reader).withType(AutomaticExemption.class).withIgnoreLeadingWhiteSpace(true).build();

            // convert `CsvToBean` object to list of users
            List<AutomaticExemption> automaticExemptionList = csvToBean.parse();
            // TODO: save to DB
 
            String filename = attachedFile.getOriginalFilename();
            Long fileSize = attachedFile.getSize();
            String fileExtension = Objects.requireNonNull(filename).substring(filename.lastIndexOf(".") + 1, filename.length()).toUpperCase();

            FileUploadDTO fileUploadDTO = FileUploadToExternalLocation.fileUploadPathRetriever(request);
            String fileUrl = fileUploadDTO.getUploadFilePath().concat(filename);
            if (!filename.equals("")) {
                ResponseMessage responseMessage = FileUploadToExternalLocation.fileUploader(attachedFile, filename, "attachFile.properties", request);

                AutoExemptionUploadedFile autoExemptionUploadedFile = new AutoExemptionUploadedFile();
                autoExemptionUploadedFile.setFileName(filename);
                autoExemptionUploadedFile.setFileUrl(fileUrl);
                autoExemptionUploadedFile.setFileExtension(fileExtension);
                autoExemptionUploadedFile.setFileSize(fileSize.toString());
                iAutoExemptionUploadedFileRepository.save(autoExemptionUploadedFile);
            }

        } catch (Exception ex) {
            ResponseEntity.badRequest().body(new MessageResponse("An error occurred while processing the CSV file."));
        }

        return ResponseEntity.ok(new MessageResponse("Uploaded successfully."));
    }
}
