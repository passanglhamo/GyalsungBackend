package com.microservice.erp.domain.dto.enrolment;

import com.microservice.erp.domain.dto.deferment.DefermentDto;
import com.microservice.erp.domain.dto.deferment.DefermentFileDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Setter
@Getter
public class EnrolmentDto {
    private Long userId;
    private List<EnrolmentCourses> enrolmentCourses;
}
