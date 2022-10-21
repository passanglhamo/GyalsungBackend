package com.microservice.erp.domain.dto.enrolment;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter
@Getter
public class EnrolmentFileDto {
//    private  MultipartFile uploadedFiles;
    private MultipartFile attachedFile;
}
