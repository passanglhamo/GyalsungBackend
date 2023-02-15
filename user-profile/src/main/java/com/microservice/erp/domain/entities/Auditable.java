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

    @CreatedDate
    @Column(name = "created_date")
    LocalDate createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    LocalDate lastModifiedDate;

    @AttributeOverride(name = "username", column = @Column(name = "created_by", columnDefinition = "bigint"))
    @Embedded
    @CreatedBy
    Username createdBy;

    @AttributeOverride(name = "username", column = @Column(name = "last_modified_by",columnDefinition = "bigint"))
    @Embedded
    @LastModifiedBy
    @Column(columnDefinition = "bigint")
    Username lastModifiedBy;

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDate lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Username getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Username createdBy) {
        this.createdBy = createdBy;
    }

    public Username getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Username lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
}
