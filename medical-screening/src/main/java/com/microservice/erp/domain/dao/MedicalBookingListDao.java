package com.microservice.erp.domain.dao;

import com.microservice.erp.domain.dto.MedicalBookingListDto;
import com.microservice.erp.domain.entities.HospitalScheduleDate;
import com.microservice.erp.domain.helper.BaseDao;
import org.hibernate.SQLQuery;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Repository
public class MedicalBookingListDao extends BaseDao {

    private final Environment environment;

    public MedicalBookingListDao(Environment environment) {
        this.environment = environment;
    }


    @Transactional
    public List<MedicalBookingListDto> getAllBookingDateByHospitalIdAndYear(BigInteger hospitalId, BigInteger year) {
        String sqlQuery = environment.getProperty("CommonDao.getAllBookingDateByHospitalIdAndYear");
        NativeQuery hQuery = (NativeQuery) hibernateQuery(sqlQuery, MedicalBookingListDto.class)
                .setParameter("hospitalId", hospitalId)
                .setParameter("year", year);
        return hQuery.list().isEmpty() ? null : hQuery.list();
    }

    @Transactional
    public List<MedicalBookingListDto> getTimeSlotsByScheduleDateId(BigInteger hospitalScheduleDateId) {
        String sqlQuery = environment.getProperty("CommonDao.getTimeSlotsByScheduleDateId");
        NativeQuery hQuery = (NativeQuery) hibernateQuery(sqlQuery, MedicalBookingListDto.class)
                .setParameter("hospitalScheduleDateId", hospitalScheduleDateId);
        return hQuery.list().isEmpty() ? null : hQuery.list();
    }
}
