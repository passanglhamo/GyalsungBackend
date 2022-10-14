package com.microservice.erp.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Setter
@Getter
@Entity(name = "change_mobile_no_sms_otp")
public class ChangeMobileNoSmsOtp {
    //region private variables
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "mobile_no", columnDefinition = "varchar(50)")
    private String mobileNo;

    @Column(name = "otp", columnDefinition = "varchar(4)")
    private String otp;
    //endregion

}
