package com.microservice.erp.domain.entities;

import com.microservice.erp.domain.helper.BaseEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "ede_deferment_file_info")
public class DefermentFileInfo extends BaseEntity {

    @Id
    @Column(name = "deferment_file_id", columnDefinition = "bigint")
    private BigInteger defermentFileId;

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
    @Column(name = "file")
    private byte[] file;

    @ManyToOne
    @JoinColumn(name = "deferment_id", nullable = false, columnDefinition = "bigint")
    private DefermentInfo deferment;

    public DefermentFileInfo(BigInteger defermentFileId, String fileSize,
                             String fileName, DefermentInfo deferment,BigInteger createdBy,
                             Date createdDate,byte[] file) {
        this.defermentFileId = defermentFileId;
        this.fileSize = fileSize;
        this.fileName = fileName;
        this.deferment = deferment;
        this.file = file;

        this.setCreatedBy(createdBy);
        this.setCreatedDate(createdDate);
    }


}
