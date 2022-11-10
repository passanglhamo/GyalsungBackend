package com.microservice.erp.services.impl;

import com.microservice.erp.domain.repositories.IHospitalScheduleDateRepository;
import com.microservice.erp.domain.repositories.IHospitalScheduleTimeRepository;
import com.microservice.erp.domain.repositories.IMedicalSelfDeclarationRepository;
import com.microservice.erp.services.iServices.IMedicalBookingListService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MedicalBookingListService implements IMedicalBookingListService {
    private final IHospitalScheduleDateRepository iHospitalScheduleDateRepository;
    private final IHospitalScheduleTimeRepository iHospitalScheduleTimeRepository;
    private final IMedicalSelfDeclarationRepository iMedicalSelfDeclarationRepository;

    @Override
    public ResponseEntity<?> getAllBookingByHospitalIdAndYear(Long hospitalId, String year) {


        return null;
    }
}
