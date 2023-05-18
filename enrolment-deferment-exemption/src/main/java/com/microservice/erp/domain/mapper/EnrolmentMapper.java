package com.microservice.erp.domain.mapper;

import com.microservice.erp.domain.dto.enrolment.EnrolmentDto;
import com.microservice.erp.domain.entities.EnrolmentCoursePreference;
import com.microservice.erp.domain.entities.EnrolmentInfo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class EnrolmentMapper {

    public EnrolmentInfo mapToEntity(EnrolmentDto enrolmentDto) {
        EnrolmentInfo enrolmentInfo = new ModelMapper().map(enrolmentDto, EnrolmentInfo.class);
//        enrolmentInfo.setEnrolmentCoursePreferences(
//                enrolmentDto.getEnrolmentCoursesDtos()
//                        .stream()
//                        .map(ta ->
//                                new EnrolmentCoursePreference(
//                                        ta.getCourseId(),
//                                        enrolmentDto.getUserId(),
//                                        ta.getPreferenceNumber(),
//                                        enrolmentInfo
//                                )
//                        )
//                        .collect(Collectors.toSet())
//        );
        return enrolmentInfo;
    }
}
