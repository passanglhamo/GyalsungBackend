package com.microservice.erp.domain.entities;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable<ID, VERSION> extends Persistable<ID, VERSION> {

    @AttributeOverride(name = "username", column = @Column(name = "created_by"))
    @Embedded @CreatedBy
    Username createdBy;

    @CreatedDate
    @Column(name = "created_date")
    LocalDateTime createdDate;

    @AttributeOverride(name = "username", column = @Column(name = "last_modified_by"))
    @Embedded @LastModifiedBy
    Username lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    LocalDateTime lastModifiedDate;

}
