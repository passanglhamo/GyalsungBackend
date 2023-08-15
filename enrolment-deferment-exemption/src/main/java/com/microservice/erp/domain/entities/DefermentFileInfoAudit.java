package com.microservice.erp.domain.entities;

import com.microservice.erp.domain.helper.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
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
    @NotNull(message = "File path cannot be null")
    @Column(name = "file_path", columnDefinition = "varchar(255)")
    private String filePath;

    @Basic(optional = false)
    @NotNull(message = "File size cannot be null")
    @Column(name = "file_size", columnDefinition = "varchar(255)")
    private String fileSize;

    @Basic(optional = false)
    @NotNull(message = "File name cannot be null")
    @Column(name = "file_name", columnDefinition = "varchar(255)")
    private String fileName;

    @ManyToOne
    @JoinColumn(name = "deferment_audit_id", nullable = false, columnDefinition = "bigint")
    private DefermentInfoAudit defermentAudit;

    public DefermentFileInfoAudit(BigInteger defermentFileAuditId, String filePath, String fileSize,
                                  String fileName, DefermentInfoAudit defermentAudit, BigInteger createdBy,
                                  Date createdDate) {
        this.defermentFileAuditId = defermentFileAuditId;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.fileName = fileName;
        this.defermentAudit = defermentAudit;

        this.setCreatedBy(createdBy);
        this.setCreatedDate(createdDate);
    }
}
