package com.microservice.erp.domain.entities;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable<ID, VERSION> extends Persistable<ID, VERSION> {

    @AttributeOverride(name = "username", column = @Column(name = "created_by",columnDefinition = "bigint"))
    @Embedded @CreatedBy
    Username createdBy;

    @CreatedDate
    LocalDate createdDate;

    @AttributeOverride(name = "username", column = @Column(name = "last_modified_by",columnDefinition = "bigint"))
    @Embedded @LastModifiedBy
    Username lastModifiedBy;

    @LastModifiedDate
    LocalDate lastModifiedDate;

}
