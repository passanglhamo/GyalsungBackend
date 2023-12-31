package com.microservice.erp.domain.mapper;

import com.microservice.erp.domain.dto.ExemptionDto;
import com.microservice.erp.domain.dto.ExemptionFileDto;
import com.microservice.erp.domain.entities.ExemptionFileInfo;
import com.microservice.erp.domain.entities.ExemptionInfo;
import com.microservice.erp.domain.helper.ApprovalStatus;
import com.microservice.erp.domain.helper.FileUploadDTO;
import com.microservice.erp.domain.helper.FileUploadToExternalLocation;
import com.microservice.erp.domain.repositories.IExemptionFileInfoRepository;
import com.microservice.erp.domain.repositories.IExemptionInfoRepository;
import com.microservice.erp.services.iServices.ICreateExemptionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ExemptionMapper {
    private final IExemptionInfoRepository repository;
    private final IExemptionFileInfoRepository fileInfoRepository;

    public ExemptionInfo mapToEntity(HttpServletRequest request, ICreateExemptionService.CreateExemptionCommand command, String caseNumber) {

        ExemptionInfo exemption = new ModelMapper().map(command, ExemptionInfo.class);
        exemption.setStatus(ApprovalStatus.PENDING.value());
        ExemptionInfo exemptionDb = repository.findFirstByOrderByExemptionIdDesc();
        BigInteger exemptionId = exemptionDb == null ? BigInteger.ONE : exemptionDb.getExemptionId().add(BigInteger.ONE);
        exemption.setExemptionId(exemptionId);
        LocalDate currentDate = LocalDate.now();
        exemption.setApplicationDate(new Date());
        exemption.setCaseNumber(caseNumber);
        exemption.setExemptionYear(String.valueOf(currentDate.getYear()));
        exemption.setCreatedBy(command.getUserId());
        exemption.setCreatedDate(new Date());

        ExemptionFileInfo exemptionFileDb = fileInfoRepository.findFirstByOrderByExemptionFileIdDesc();
        BigInteger exemptionFileId = exemptionFileDb == null ? BigInteger.ZERO : exemptionFileDb.getExemptionFileId();
        final BigInteger[] initialNo = {BigInteger.ZERO};

        if (!Objects.isNull(command.getProofDocuments())) {
            exemption.setFiles(
                    Arrays.stream(command.getProofDocuments())
                            .map(t ->
                            {
                                initialNo[0] = initialNo[0].add(BigInteger.ONE);

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
                                            exemptionFileId.add(initialNo[0]),
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
                exemption.getExemptionYear(),
                exemption.getExemptionId(),
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
                                        ta.getExemptionFileId(),
                                        ta.getFilePath(),
                                        ta.getFileSize(),
                                        ta.getFileName()
                                )
                        )
                        .collect(Collectors.toUnmodifiableSet()),
                null,
                null,
                null,
                exemption.getGender(),
                exemption.getApplicationDate(),
                exemption.getCaseNumber());
    }
}
