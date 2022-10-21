package com.microservice.erp.domain.entities;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author Rajib Kumer Ghosh
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "ede_enrolment_file_info")
@AttributeOverride(name = "id", column = @Column(name = "enrolment_file_id"))
public class EnrolmentFileInfo extends Auditable<Long, Long> {

    @Basic(optional = false)
    @NotNull(message = "File path cannot not be null")
    @Column(name = "file_path", columnDefinition = "varchar(255)")
    private String filePath;

    @Basic(optional = false)
    @NotNull(message = "File size cannot not be null")
    @Column(name = "file_size", columnDefinition = "varchar(255)")
    private String fileSize;

    @Basic(optional = false)
    @NotNull(message = "File size cannot not be null")
    @Column(name = "file_name", columnDefinition = "varchar(255)")
    private String fileName;

    @Basic(optional = false)
    @NotNull(message = "File size cannot not be null")
    @Column(name = "file_extension", columnDefinition = "varchar(255)")
    private String fileExtension;

    @ManyToOne
    @JoinColumn(name = "enrolment_id", nullable = false)
    private EnrolmentInfo enrolment;

}
