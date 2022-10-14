package com.microservice.erp.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Setter
@Getter
@Entity(name = "change_email_verification_code")
public class ChangeEmailVerificationCode {
    //region private variables
    @Id
    @Column(name = "userId")
    private Long userId;

    @Column(name = "email", columnDefinition = "varchar(255)")
    private String email;

    @Column(name = "verificationCode", columnDefinition = "varchar(6)")
    private String verificationCode;
    //endregion

}
