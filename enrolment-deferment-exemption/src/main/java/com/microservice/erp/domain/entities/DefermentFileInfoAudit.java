package com.microservice.erp.domain.entities;

import com.microservice.erp.domain.helper.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.sql.Blob;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "ede_deferment_file_info_a")
public class DefermentFileInfoAudit extends BaseEntity {

    @Id
    @Column(name = "deferment_file_audit_id", columnDefinition = "bigint")
    private BigInteger defermentFileAuditId;


    @Basic(optional = false)
    @NotNull(message = "File size cannot be null")
    @Column(name = "file_size", columnDefinition = "varchar(255)")
    private String fileSize;

    @Basic(optional = false)
    @NotNull(message = "File name cannot be null")
    @Column(name = "file_name", columnDefinition = "varchar(255)")
    private String fileName;

    @Basic(optional = false)
    @NotNull(message = "File  cannot be null")
    @Column(name = "file",columnDefinition = "bytea")
    private byte[] file;

    @ManyToOne
    @JoinColumn(name = "deferment_audit_id", nullable = false, columnDefinition = "bigint")
    private DefermentInfoAudit defermentAudit;

    public DefermentFileInfoAudit(BigInteger defermentFileAuditId, String fileSize,
                                  String fileName, DefermentInfoAudit defermentAudit, BigInteger createdBy,
                                  Date createdDate,byte[] file) {
        this.defermentFileAuditId = defermentFileAuditId;
        this.fileSize = fileSize;
        this.fileName = fileName;
        this.defermentAudit = defermentAudit;
        this.file = file;

        this.setCreatedBy(createdBy);
        this.setCreatedDate(createdDate);
    }
}
