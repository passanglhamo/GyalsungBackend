package com.microservice.erp.services.impl;

import com.microservice.erp.domain.dto.AutoExemptionDto;
import com.microservice.erp.domain.entities.AutoExemptionFile;
import com.microservice.erp.domain.entities.AutoExemption;
import com.microservice.erp.domain.helper.FileUploadDTO;
import com.microservice.erp.domain.helper.FileUploadToExternalLocation;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.helper.ResponseMessage;
import com.microservice.erp.domain.repositories.IAutoExemptionRepository;
import com.microservice.erp.domain.repositories.IAutoExemptionFileRepository;
import com.microservice.erp.services.iServices.IAutoExemptionService;
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
public class AutoExemptionService implements IAutoExemptionService {
    private final IAutoExemptionRepository iAutoExemptionRepository;
    private final IAutoExemptionFileRepository iAutoExemptionFileRepository;

    public AutoExemptionService(IAutoExemptionRepository iAutoExemptionRepository, IAutoExemptionFileRepository iAutoExemptionFileRepository) {
        this.iAutoExemptionRepository = iAutoExemptionRepository;
        this.iAutoExemptionFileRepository = iAutoExemptionFileRepository;
    }

    @Override
    public ResponseEntity<?> readFile(AutoExemptionDto autoExemptionDto) {
        MultipartFile attachedFile = autoExemptionDto.getAttachedFile();
        List<AutoExemption> autoExemptionList = new ArrayList<>();
        try (Reader reader = new BufferedReader(new InputStreamReader(attachedFile.getInputStream()))) {
            CsvToBean csvToBean = new CsvToBeanBuilder(reader).withType(AutoExemption.class).withIgnoreLeadingWhiteSpace(true).build();
            autoExemptionList = csvToBean.parse();
        } catch (Exception ex) {
            ResponseEntity.badRequest().body(new MessageResponse("An error occurred while processing the CSV file."));
        }
        return ResponseEntity.ok(autoExemptionList);
    }

    @Override
    public ResponseEntity<?> uploadFile(HttpServletRequest request, AutoExemptionDto autoExemptionDto) throws IOException {
        MultipartFile attachedFile = autoExemptionDto.getAttachedFile();
        try (Reader reader = new BufferedReader(new InputStreamReader(attachedFile.getInputStream()))) {
            CsvToBean csvToBean = new CsvToBeanBuilder(reader).withType(AutoExemption.class).withIgnoreLeadingWhiteSpace(true).build();
            List<AutoExemption> autoExemptionList = csvToBean.parse();
            iAutoExemptionRepository.saveAll(autoExemptionList);
            String filename = attachedFile.getOriginalFilename();
            Long fileSize = attachedFile.getSize();
            String fileExtension = Objects.requireNonNull(filename).substring(filename.lastIndexOf(".") + 1, filename.length()).toUpperCase();
            FileUploadDTO fileUploadDTO = FileUploadToExternalLocation.fileUploadPathRetriever(request);
            String fileUrl = fileUploadDTO.getUploadFilePath().concat(filename);
            if (!filename.equals("")) {
                ResponseMessage responseMessage = FileUploadToExternalLocation.fileUploader(attachedFile, filename, "attachFile.properties", request);
                AutoExemptionFile autoExemptionFile = new AutoExemptionFile();
                autoExemptionFile.setFileName(filename);
                autoExemptionFile.setFileUrl(fileUrl);
                autoExemptionFile.setFileExtension(fileExtension);
                autoExemptionFile.setFileSize(fileSize.toString());
                iAutoExemptionFileRepository.save(autoExemptionFile);
            }
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(new MessageResponse("An error occurred while processing the CSV file. A person with the same CID might have already uploaded, or your CSV file might have a duplicate CID. Please review the \"exempted list\" and remove that individual who already exists in the \"exempted list.\" Your CSV file must include the column names cid, fullName, dateOfBirth, and gender."));
        }

        return ResponseEntity.ok(new MessageResponse("Uploaded successfully."));
    }
}
