//package com.microservice.erp.domain.entities;
//
//import lombok.Getter;
//import lombok.Setter;
//
//import javax.persistence.*;
//import java.math.BigInteger;
//import java.util.Date;
//
//@Setter
//@Getter
//@Entity
//@Table(name = "sa_pw_change_request")
//public class PwChangeRequest {
//    //region private variables
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "request_id", columnDefinition = "bigint")
//    private BigInteger requestId;
//
//    @Column(name = "status", columnDefinition = "char(1)")
//    private Character status;
//
//    @Column(name = "email", columnDefinition = "varchar(255)")
//    private String email;
//
//    @Column(name = "updated_by", columnDefinition = "bigint")
//    private BigInteger updatedBy;
//
//    @Column(name = "updated_date", columnDefinition = "date")
//    private Date updatedDate;
//
//    @Column(name = "created_by", columnDefinition = "bigint")
//    private BigInteger createdBy;
//
//    @Column(name = "created_date", columnDefinition = "date")
//    private Date createdDate;
//    //endregion
//}
