package com.microservice.erp.domain.mapper;


import com.microservice.erp.domain.dto.DefermentDto;
import com.microservice.erp.domain.dto.DefermentFileDto;
import com.microservice.erp.domain.entities.DefermentFileInfo;
import com.microservice.erp.domain.entities.DefermentInfo;
import com.microservice.erp.domain.helper.ApprovalStatus;
import com.microservice.erp.domain.helper.FileUploadDTO;
import com.microservice.erp.domain.helper.FileUploadToExternalLocation;
import com.microservice.erp.domain.repositories.IDefermentFileInfoRepository;
import com.microservice.erp.domain.repositories.IDefermentInfoRepository;
import com.microservice.erp.services.iServices.ICreateDefermentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DefermentMapper {
    private final IDefermentInfoRepository repository;

    public DefermentInfo mapToEntity(HttpServletRequest request, ICreateDefermentService.CreateDefermentCommand command,
                                     String caseNumber) {

        DefermentInfo deferment = new ModelMapper().map(command, DefermentInfo.class);
        DefermentInfo defermentDb = repository.findFirstByOrderByDefermentIdDesc();
        BigInteger defermentId = defermentDb == null ? BigInteger.ONE : defermentDb.getDefermentId().add(BigInteger.ONE);
        deferment.setDefermentId(defermentId);
        deferment.setStatus(ApprovalStatus.PENDING.value());
        deferment.setApplicationDate(new Date());
        deferment.setCaseNumber(caseNumber);
        deferment.setCreatedBy(command.getUserId());
        deferment.setCreatedDate(new Date());
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
                deferment.getDefermentId(),
                deferment.getDefermentYear(),
                deferment.getUserId(),
                deferment.getReasonId(),
                deferment.getApprovalRemarks(),
                deferment.getApplicationDate(),
                deferment.getStatus(),
                deferment.getRemarks(),
                null,
                deferment.getFiles().size() == 0 ? null :
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
                deferment.getGender(),
                deferment.getApplicationDate(),
                deferment.getCaseNumber()
        );
    }
}
