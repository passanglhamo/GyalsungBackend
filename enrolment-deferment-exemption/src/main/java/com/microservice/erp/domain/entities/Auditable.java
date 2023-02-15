package com.microservice.erp.domain.entities;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable<ID, VERSION> extends Persistable<ID, VERSION> {

    @AttributeOverride(name = "username", column = @Column(name = "created_by",columnDefinition = "bigint"))
    @Embedded @CreatedBy
    Username createdBy;

    @CreatedDate
    @Column(name = "created_date")
    LocalDateTime createdDate;

    @AttributeOverride(name = "username", column = @Column(name = "last_modified_by",columnDefinition = "bigint"))
    @Embedded @LastModifiedBy
    Username lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    LocalDateTime lastModifiedDate;

    public Username getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Username createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Username getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Username lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
