package com.microservice.erp.domain.helper;

public class FileUploadDTO {
	private Double maxFileSize;
	private String supportedFileType[];
	//used for error message
	private String supportedFileTypeInString;
	private String uploadFilePath;

	public Double getMaxFileSize(){return maxFileSize;}
	public void setMaxFileSize(Double maxFileSize) { this.maxFileSize = maxFileSize;}
	public String[] getSupportedFileType() { return supportedFileType;}
	public void setSupportedFileType(String[] supportedFileType) {this.supportedFileType = supportedFileType;}
	
	public String getSupportedFileTypeInString() { return supportedFileTypeInString;}
	public void setSupportedFileTypeInString (String supportedFileTypeInString){ 
		this.supportedFileTypeInString = supportedFileTypeInString;
	}

    public String getUploadFilePath() {
        return uploadFilePath;
    }

    public void setUploadFilePath(String uploadFilePath) {
        this.uploadFilePath = uploadFilePath;
    }
}