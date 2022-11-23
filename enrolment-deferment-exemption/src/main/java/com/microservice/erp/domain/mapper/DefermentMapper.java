package com.microservice.erp.domain.mapper;


import com.microservice.erp.domain.dto.DefermentDto;
import com.microservice.erp.domain.dto.DefermentFileDto;
import com.microservice.erp.domain.entities.DefermentFileInfo;
import com.microservice.erp.domain.entities.DefermentInfo;
import com.microservice.erp.domain.helper.ApprovalStatus;
import com.microservice.erp.domain.helper.FileUploadDTO;
import com.microservice.erp.domain.helper.FileUploadToExternalLocation;
import com.microservice.erp.services.iServices.ICreateDefermentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class DefermentMapper {
    public DefermentInfo mapToEntity(HttpServletRequest request, ICreateDefermentService.CreateDefermentCommand command) {

        DefermentInfo deferment = new ModelMapper().map(command, DefermentInfo.class);
        deferment.setStatus(ApprovalStatus.PENDING.value());
        if (!Objects.isNull(command.getProofDocuments())) {
            deferment.setFiles(
                    Arrays.stream(command.getProofDocuments())
                            .map(t ->
                            {

                                try {
                                    String filename = t.getOriginalFilename();
                                    Long fileSize = t.getSize();
                                    FileUploadDTO fileUploadDTO = FileUploadToExternalLocation.fileUploadPathRetriever(request);
                                    String fileUrl = fileUploadDTO.getUploadFilePath().concat(filename);
                                    BigDecimal size = (new BigDecimal(fileSize).divide(new BigDecimal(1024)));
                                    Integer length = (fileSize.toString()).length();

                                    FileUploadToExternalLocation.fileUploader(t, filename, "fileConfig/attachFile.properties", request);
                                    String finalSize = null;
                                    DecimalFormat df = new DecimalFormat("#.00");
                                    if (length == 6) {
                                        finalSize = df.format(size).toString() + "KB";
                                    } else {
                                        size = (size.divide(new BigDecimal(1024)));
                                        finalSize = df.format(size).toString() + "MB";
                                    }
                                    return new DefermentFileInfo(
                                            fileUrl,
                                            finalSize,
                                            filename,
                                            deferment
                                    );
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }


                            }).collect(Collectors.toSet())


            );
        }


        return deferment;
    }

    public DefermentDto mapToDomain(DefermentInfo deferment) {
        return DefermentDto.withId(
                deferment.getId(),
                deferment.getFromDate(),
                deferment.getUserId(),
                deferment.getReasonId(),
                deferment.getApprovalRemarks(),
                deferment.getToDate(),
                deferment.getStatus(),
                deferment.getRemarks(),
                null,
                deferment.getFiles()
                        .stream()
                        .map(ta ->
                                DefermentFileDto.withId(
                                        ta.getId(),
                                        ta.getFilePath(),
                                        ta.getFileSize(),
                                        ta.getFileName()
                                )
                        )
                        .collect(Collectors.toUnmodifiableSet())
                ,
                null,
                null,
                null,
                null
        );
    }
}
