package com.microservice.erp.domain.mapper.exemption;

import com.microservice.erp.domain.dto.exemption.ExemptionDto;
import com.microservice.erp.domain.dto.exemption.ExemptionFileDto;
import com.microservice.erp.domain.entities.ExemptionFileInfo;
import com.microservice.erp.domain.entities.ExemptionInfo;
import com.microservice.erp.services.helper.ApprovalStatus;
import com.microservice.erp.services.helper.FileUploadDTO;
import com.microservice.erp.services.helper.FileUploadToExternalLocation;
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
public class ExemptionMapper {
    public ExemptionInfo mapToEntity(HttpServletRequest request, ExemptionDto exemptionDto) {

        ExemptionInfo exemption = new ModelMapper().map(exemptionDto, ExemptionInfo.class);
        exemption.setStatus(ApprovalStatus.PENDING.value());
        exemption.setUserId(exemptionDto.getUserId());
        if(!Objects.isNull(exemptionDto.getProofDocuments())) {
            exemption.setFiles(
                    Arrays.stream(exemptionDto.getProofDocuments())
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
                                    //todo need to update code
                                    String finalSize = null;
                                    DecimalFormat df = new DecimalFormat("#.00");
                                    if (length == 6) {
                                        finalSize = df.format(size).toString() + "KB";
                                    } else {
                                        size = (size.divide(new BigDecimal(1024)));
                                        finalSize = df.format(size).toString() + "MB";
                                    }
                                    return new ExemptionFileInfo(
                                            fileUrl,
                                            finalSize,
                                            filename,
                                            exemption
                                    );
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }


                            }).collect(Collectors.toSet())


            );
        }

        return exemption;
    }

    public ExemptionDto mapToDomain(ExemptionInfo exemption) {
        return ExemptionDto.withId(
                exemption.getId(),
                exemption.getUserId(),
                exemption.getReasonId(),
                exemption.getApprovalRemarks(),
                exemption.getStatus(),
                exemption.getRemarks(),
                null,
                exemption.getFiles()
                        .stream()
                        .map(ta ->
                                ExemptionFileDto.withId(
                                        ta.getId(),
                                        ta.getFilePath(),
                                        ta.getFileSize(),
                                        ta.getFileName()
                                )
                        )
                        .collect(Collectors.toUnmodifiableSet()));
    }
}
