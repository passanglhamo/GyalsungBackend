package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class UserInfoDto {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String sex;
    private Date dob;
    private String cid;
    private String mobileNo;
    private String employeeId;
    private String defaultBranchCode;
    private Boolean passwordChangedYN;
    private Boolean userForcePasswordStrengthYN;
    private Boolean userLockStatus;
    private String presentCountry;
    private Integer presentDzongkhagId;
    private Integer presentGeogId;
    private String presentPlaceName;
    private String permanentCountry;
    private String permanentDzongkhag;
    private String permanentGeog;
    private String permanentPlaceName;
    private String fatherName;
    private String fatherCid;
    private String fatherMobileNo;
    private String fatherOccupation;
    private String fatherEmail;
    private String motherName;
    private String motherCid;
    private String motherMobileNo;
    private String motherOccupation;
    private String motherEmail;
    private String guardianName;
    private String guardianCid;
    private String guardianMobileNo;
    private String guardianOccupation;
    private String guardianEmail;
    private String relationToGuardian;
    private String socialMediaLink1;
    private String socialMediaLink2;
    private String socialMediaLink3;
    private String profilePictureName;
    private String profilePictureUrl;
    private String profilePictureExt;
    private String profilePictureSize;
    private String remarks;
    private Character status;
}
