package com.microservice.erp.domain.entities;

import com.microservice.erp.domain.helper.BaseEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "ede_deferment_file_info")
//@AttributeOverride(name = "id", column = @Column(name = "deferment_file_id", columnDefinition = "bigint"))
public class DefermentFileInfo extends BaseEntity {

    @Id
    @Column(name = "deferment_file_id", columnDefinition = "bigint")
    private BigInteger defermentFileId;

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
    @JoinColumn(name = "deferment_id", nullable = false, columnDefinition = "bigint")
    private DefermentInfo deferment;
}
