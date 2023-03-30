package com.microservice.erp.domain.dao;

import com.microservice.erp.domain.dto.BookHospitalDto;
import com.microservice.erp.domain.dto.EnrolmentListDto;
import com.microservice.erp.domain.entities.HospitalBooking;
import com.microservice.erp.domain.helper.BaseDao;
import org.hibernate.query.NativeQuery;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Repository
public class BookHospitalDao extends BaseDao {

    private final Environment environment;

    public BookHospitalDao(Environment environment) {
        this.environment = environment;
    }

    @Transactional
    public List<BookHospitalDto> getAllBookingByYearAndHospitalId(BigInteger year, Integer hospitalId) {
        String sqlQuery = environment.getProperty("CommonDao.getAllBookingByYearAndHospitalId");
        NativeQuery hQuery = (NativeQuery) hibernateQuery(sqlQuery, BookHospitalDto.class)
                .setParameter("year", year)
                .setParameter("hospitalId", hospitalId);
        return hQuery.list().isEmpty() ? null : hQuery.list();
    }
}
