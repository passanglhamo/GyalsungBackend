package com.microservice.erp.domain.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigInteger;
import java.util.Date;

@Entity(name = "ede_hospital_booking")
@AttributeOverride(name = "id", column = @Column(name = "hospital_booking_id", columnDefinition = "bigint"))
public class HospitalBooking extends Auditable<BigInteger, Long> {

    //region private variables
    @Column(name = "user_id", unique = true, columnDefinition = "bigint")
    private BigInteger userId;

    @Column(name = "dzongkhag_id", columnDefinition = "int")
    private Integer dzongkhagId;

    @Column(name = "hospital_id", columnDefinition = "int")
    private Integer hospitalId;

    @Column(name = "screening_date", columnDefinition = "date")
    private Date screeningDate;

    //endregion

    //region setters and getters

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public Integer getDzongkhagId() {
        return dzongkhagId;
    }

    public void setDzongkhagId(Integer dzongkhagId) {
        this.dzongkhagId = dzongkhagId;
    }

    public Integer getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Integer hospitalId) {
        this.hospitalId = hospitalId;
    }

    public Date getScreeningDate() {
        return screeningDate;
    }

    public void setScreeningDate(Date screeningDate) {
        this.screeningDate = screeningDate;
    }

    //endregion
}
