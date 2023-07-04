package com.microservice.erp.services.impl.services;

import com.microservice.erp.domain.dto.HospitalBookingDateDto;
import com.microservice.erp.domain.dto.HospitalBookingDetailsDto;
import com.microservice.erp.domain.dto.HospitalScheduleDateDto;
import com.microservice.erp.domain.entities.HospitalBookingDate;
import com.microservice.erp.domain.entities.HospitalBookingDetail;
import com.microservice.erp.domain.entities.HospitalScheduleDate;
import com.microservice.erp.domain.helper.MessageResponse;
import com.microservice.erp.domain.repositories.IHospitalBookingDateRepository;
import com.microservice.erp.domain.repositories.IHospitalBookingDetailsRepository;
import com.microservice.erp.domain.repositories.IHospitalScheduleDateRepository;
import com.microservice.erp.services.iServices.IReadHospitalScheduleDateService;
import com.microservice.erp.services.impl.mapper.HospitalBookingDateMapper;
import com.microservice.erp.services.impl.mapper.HospitalScheduleTimeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReadHospitalScheduleDateService implements IReadHospitalScheduleDateService {

    private final IHospitalScheduleDateRepository repository;
    private final IHospitalBookingDateRepository bookingDateRepository;
    private final HospitalScheduleTimeMapper mapper;
    private final HospitalBookingDateMapper hospitalBookingDateMapper;
    private final IHospitalBookingDetailsRepository iHospitalBookingDetailsRepository;

    public Collection<HospitalScheduleDateDto> getAllScheduleDateById(BigInteger dzoHosId) {
        return repository.findAllByHospitalId(dzoHosId)
                .stream()
                .map(mapper::mapToDomain)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public ResponseEntity<?> getAllAvailableAppointmentDateByHospitalId(BigInteger hospitalId) {
        Collection<HospitalScheduleDate> hospitalScheduleTimes = repository.findByHospitalIdOrderByAppointmentDateAsc(hospitalId);
        if (hospitalScheduleTimes.size() > 0) {
            return ResponseEntity.ok(hospitalScheduleTimes
                    .stream()
                    .map(mapper::mapToDomain)
                    .collect(Collectors.toUnmodifiableList()));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Data not found"));
        }
    }

    @Override
    public List<HospitalBookingDateDto> getAllAppointmentDateByHospitalId(BigInteger hospitalId) {
        return bookingDateRepository.findAllByHospitalId(hospitalId)
                .stream()
                .map(hospitalBookingDateMapper::mapToDomain)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public HospitalBookingDateDto getHospitalBookingDetailByBookingId(String authHeader, BigInteger hospitalId,
                                                                      Date appointmentDate) {
        HospitalBookingDate hospitalBookingDate = bookingDateRepository.findByHospitalIdAndAppointmentDate(hospitalId, appointmentDate);

        List<HospitalBookingDetail> getBookedUserAm = iHospitalBookingDetailsRepository.findAllByHospitalBookingIdAndAmPm(
                hospitalBookingDate.getId(), 'A'
        );
        Integer countAmBooked = Objects.isNull(getBookedUserAm) ? 0 : getBookedUserAm.size();

        List<HospitalBookingDetail> getBookedUserPm = iHospitalBookingDetailsRepository.findAllByHospitalBookingIdAndAmPm(
                hospitalBookingDate.getId(), 'P'
        );
        Integer countPmBooked = Objects.isNull(getBookedUserPm) ? 0 : getBookedUserPm.size();

        return HospitalBookingDateDto.withId(
                hospitalBookingDate.getId(),
                hospitalBookingDate.getHospitalId(),
                hospitalBookingDate.getAppointmentDate(),
                hospitalBookingDate.getAmSlots(),
                (hospitalBookingDate.getAmSlots() - countAmBooked),
                hospitalBookingDate.getPmSlots(),
                (hospitalBookingDate.getPmSlots() - countPmBooked),
                hospitalBookingDate.getStatus()
        );
    }

    @Override
    public HospitalBookingDetailsDto getHospitalBookingDetailByUserId(String authHeader, BigInteger userId) {
        HospitalBookingDetail hospitalBookingDetail = iHospitalBookingDetailsRepository.findByUserId(userId);
        HospitalBookingDate hospitalBookingDate = bookingDateRepository.findById(hospitalBookingDetail.getHospitalBookingId()).get();

        return HospitalBookingDetailsDto.withId(
                hospitalBookingDetail.getId(),
                hospitalBookingDetail.getHospitalBookingId(),
                hospitalBookingDate.getHospitalId(),
                hospitalBookingDate.getAppointmentDate(),
                hospitalBookingDetail.getAmPm(),
                hospitalBookingDetail.getUserId(),
                null

        );

    }
}
