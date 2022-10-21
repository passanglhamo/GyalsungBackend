package com.microservice.erp.services.impl.deferment;

import com.microservice.erp.domain.dto.deferment.DefermentDto;
import com.microservice.erp.domain.entities.DefermentFileInfo;
import com.microservice.erp.domain.mapper.deferment.DefermentMapper;
import com.microservice.erp.domain.repositories.IDefermentFileInfoRepository;
import com.microservice.erp.domain.repositories.IDefermentInfoRepository;
import com.microservice.erp.services.helper.FileUploadToExternalLocation;
import com.microservice.erp.services.helper.ResponseMessage;
import com.microservice.erp.services.helper.SystemDataInt;
import com.microservice.erp.services.iServices.deferment.IReadDefermentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReadDefermentService implements IReadDefermentService {
    private final IDefermentInfoRepository repository;
    private final IDefermentFileInfoRepository repositoryFile;
    private final DefermentMapper mapper;

    @Override
    public Collection<DefermentDto> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::mapToDomain)
                .collect(Collectors.toUnmodifiableList());

    }

    @Override
    public ResponseEntity<?> downloadFiles(Long defermentId, HttpServletResponse response) {
        DefermentFileInfo defermentFile = repositoryFile.findById(defermentId).get();
        ResponseMessage responseMessage = new ResponseMessage();
        String uploadFilePath = defermentFile.getFilePath();
        String fileName = defermentFile.getFileName();

        try {
            responseMessage = FileUploadToExternalLocation.fileDownloader(fileName, uploadFilePath, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        responseMessage.setDto(uploadFilePath);
        responseMessage.setText(uploadFilePath);
        responseMessage.setStatus(SystemDataInt.MESSAGE_STATUS_SUCCESSFUL.value());

        return ResponseEntity.ok(responseMessage);
    }
}
