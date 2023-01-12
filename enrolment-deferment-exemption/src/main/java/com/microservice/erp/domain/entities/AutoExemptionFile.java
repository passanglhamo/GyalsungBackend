package com.microservice.erp.domain.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigInteger;

@Entity(name = "ede_auto_exemption_file")
@AttributeOverride(name = "id", column = @Column(name = "file_id", columnDefinition = "bigint"))
public class AutoExemptionFile extends Auditable<BigInteger, Long> {
    //region private variables
    @Column(name = "file_name", columnDefinition = "varchar(255)")
    private String fileName;

    @Column(name = "file_url", columnDefinition = "varchar(255)")
    private String fileUrl;

    @Column(name = "file_extension", columnDefinition = "varchar(255)")
    private String fileExtension;

    @Column(name = "file_size", columnDefinition = "varchar(255)")
    private String fileSize;
    //endregion

    //region setters and getters

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    //endregion
}
