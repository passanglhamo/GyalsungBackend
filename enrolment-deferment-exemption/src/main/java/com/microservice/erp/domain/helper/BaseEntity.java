package com.microservice.erp.domain.helper;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.math.BigInteger;
import java.util.Date;

@MappedSuperclass
@Setter
@Getter
public class BaseEntity {

    //region private variables
    @Column(name = "updated_by", columnDefinition = "bigint")
    private BigInteger updatedBy;

    @Column(name = "updated_date")
    private Date updatedDate;

    @Column(name = "created_by", columnDefinition = "bigint")
    private BigInteger createdBy;

    @Column(name = "created_date")
    private Date createdDate;
    //endregion

}
