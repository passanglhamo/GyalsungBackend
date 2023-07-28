package com.microservice.erp.domain.entities;

import com.microservice.erp.domain.helper.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "sa_pw_reset_url")
public class PwResetUrl extends BaseEntity {
    //region private variables
    @Id
    @Column(name = "user_id", columnDefinition = "bigint")
    private BigInteger userId;

    @Column(name = "email", columnDefinition = "varchar(255)")
    private String email;

    @Column(name = "date")
    private Date date;
    //endregion
}
