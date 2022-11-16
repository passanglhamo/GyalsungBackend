package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Setter
@Getter
public class UserProfileDto {
    //region private variables
    private Long userId;
    private String username;
    private String fullName;
    private String sex;
    private String cid;
    private String studentCode;
    private String birthDate;
    private Date dob;
    private String mobileNo;
    private String otp;
    private String email;
    private String verificationCode;
    private String currentPassword;
    private String newPassword;
    private String password;
    private String confirmPassword;
    private String fatherName;
    private String fatherCid;
    private String motherName;
    private String motherCid;
    private String permanentCountry;
    private String permanentDzongkhag;
    private String permanentGeog;
    private String permanentPlaceName;
    private String fatherMobileNo;
    private String fatherEmail;
    private String fatherOccupation;
    private String motherMobileNo;
    private String motherEmail;
    private String motherOccupation;
    private String guardianName;
    private String guardianCid;
    private String guardianMobileNo;
    private String guardianEmail;
    private String guardianOccupation;
    private String relationToGuardian;
    private String socialMediaLink1;
    private String socialMediaLink2;
    private String socialMediaLink3;
    private Integer presentDzongkhagId;
    private String presentDzongkhagName;
    private Integer presentGeogId;
    private String presentGeogName;
    private String presentPlaceName;
    private String presentCountry;
    private MultipartFile profilePicture;
    private byte[] profilePhoto;
    //endregion
}
