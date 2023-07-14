package com.microservice.erp.domain.entities;

import com.microservice.erp.domain.helper.BaseEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

/**
 * @author Rajib Kumer Ghosh
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity(name = "ede_exemption_file_info")
//@AttributeOverride(name = "id", column = @Column(name = "exemption_file_id", columnDefinition = "bigint"))
public class ExemptionFileInfo extends BaseEntity {

    //region private variables
    @Id
    @Column(name = "exemption_file_id", columnDefinition = "bigint")
    private BigInteger exemptionFileId;

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
    @JoinColumn(name = "exemption_id", nullable = false, columnDefinition = "bigint")
    private ExemptionInfo exemption;
    //endregion
}
