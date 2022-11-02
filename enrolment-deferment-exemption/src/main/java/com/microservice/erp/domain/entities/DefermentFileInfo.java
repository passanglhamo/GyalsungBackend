package com.microservice.erp.domain.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "ede_deferment_file_info")
@AttributeOverride(name = "id", column = @Column(name = "deferment_file_id"))
public class DefermentFileInfo extends Auditable<Long, Long> {


    @Basic(optional = false)
    @NotNull(message = "File path cannot be null")
    @Column(name = "file_path",columnDefinition = "varchar(255)")
    private String filePath;

    @Basic(optional = false)
    @NotNull(message = "File size cannot be null")
    @Column(name = "file_size",columnDefinition = "varchar(255)")
    private String fileSize;

    @Basic(optional = false)
    @NotNull(message = "File name cannot be null")
    @Column(name = "file_name",columnDefinition = "varchar(255)")
    private String fileName;

//    @Basic(optional = false)
//    @NotNull
//    @Column(name = "status")
//    private String status;

    @ManyToOne
    @JoinColumn(name = "deferment_id", nullable = false)
    private DefermentInfo deferment;
}