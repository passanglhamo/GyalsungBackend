package com.microservice.erp.domain.mapper.enrolment;

import com.microservice.erp.domain.dto.enrolment.EnrolmentDto;
import com.microservice.erp.domain.entities.EnrolmentFileInfo;
import com.microservice.erp.domain.entities.EnrolmentInfo;
import com.microservice.erp.domain.helper.FileUploadDTO;
import com.microservice.erp.domain.helper.FileUploadToExternalLocation;
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
public class EnrolmentMapper {
    public EnrolmentInfo mapToEntity(HttpServletRequest request, EnrolmentDto enrolmentDto) {
        EnrolmentInfo enrolmentInfo = new ModelMapper().map(enrolmentDto, EnrolmentInfo.class);
        enrolmentInfo.setStatus('P');
        if (enrolmentDto.getEnrolmentInfoFiles() != null) {
            enrolmentInfo.setFiles(
                    Arrays.stream(enrolmentDto.getEnrolmentInfoFiles())
                            .map(t ->
                            {
                                try {
                                    String filename = t.getOriginalFilename();
                                    Long fileSize = t.getSize();
                                    String fileExtension = Objects.requireNonNull(filename).substring(filename.lastIndexOf(".") + 1, filename.length()).toUpperCase();
                                    FileUploadDTO fileUploadDTO = FileUploadToExternalLocation.fileUploadPathRetriever(request);
                                    String fileUrl = fileUploadDTO.getUploadFilePath().concat(filename);
                                    BigDecimal size = (new BigDecimal(fileSize).divide(new BigDecimal(1024)));
                                    Integer length = (fileSize.toString()).length();

                                    FileUploadToExternalLocation.fileUploader(t, filename, "fileConfig/attachFile.properties", request);
                                    String finalSize;
                                    DecimalFormat df = new DecimalFormat("#.00");
                                    if (length == 6) {
                                        finalSize = df.format(size).toString() + "KB";
                                    } else {
                                        size = (size.divide(new BigDecimal(1024)));
                                        finalSize = df.format(size).toString() + "MB";
                                    }
                                    return new EnrolmentFileInfo(
                                            fileUrl,
                                            finalSize,
                                            filename,
                                            fileExtension,
                                            enrolmentInfo
                                    );
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }).collect(Collectors.toSet())
            );
        }
        return enrolmentInfo;
    }
}
