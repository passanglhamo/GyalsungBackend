package com.microservice.erp.domain.entities;

import lombok.AllArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;

//@Setter
//@Getter
@AllArgsConstructor
@Entity(name = "sa_user")
@AttributeOverride(name = "id", column = @Column(name = "user_id", columnDefinition = "bigint"))
public class UserInfo extends Auditable<BigInteger, Long> {

    @NotNull(message = "Username must not be null.")
    @Basic(optional = false)
    @Column(name = "username", columnDefinition = "varchar(255)")
    private String username;

//    @NotNull(message = "Password cannot be null")
//    @Size(max = 250)
//    @Basic(optional = false)
//    @Column(name = "password", columnDefinition = "varchar(255)")
//    private String password;

    @Basic(optional = false)
    @NotNull(message = "Full name cannot be null")
    @Column(name = "full_name", columnDefinition = "varchar(255)")
    private String fullName;

    @NotNull(message = "Email cannot be null")
    @Column(name = "email", columnDefinition = "varchar(255)")
    private String email;

    @Column(name = "gender", columnDefinition = "char(1)")
    private Character gender;

    @Column(name = "dob")
    private Date dob;

    @Column(name = "cid", columnDefinition = "varchar(255)")
    private String cid;

    @NotNull(message = "Mobile no cannot be null")
    @Basic(optional = false)
    @Column(name = "mobile_no", columnDefinition = "varchar(255)")
    private String mobileNo;

    @Column(name = "user_lock_status", columnDefinition = "boolean")
    private Boolean userLockStatus;

    @Column(name = "present_country", columnDefinition = "varchar(255)")
    private String presentCountry;

    @Column(name = "present_dzongkhag_id", columnDefinition = "int")
    private Integer presentDzongkhagId;

    @Column(name = "present_geog_id", columnDefinition = "int")
    private Integer presentGeogId;

    @Column(name = "present_place_name", columnDefinition = "varchar(255)")
    private String presentPlaceName;

    @Column(name = "permanent_country", columnDefinition = "varchar(255)")
    private String permanentCountry;

    @Column(name = "permanent_dzongkhag", columnDefinition = "varchar(255)")
    private String permanentDzongkhag;

    @Column(name = "permanent_geog", columnDefinition = "varchar(255)")
    private String permanentGeog;

    @Column(name = "permanent_place_name", columnDefinition = "varchar(255)")
    private String permanentPlaceName;

    @Column(name = "father_name", columnDefinition = "varchar(255)")
    private String fatherName;

    @Column(name = "father_cid", columnDefinition = "varchar(255)")
    private String fatherCid;

    @Column(name = "father_mobile_no", columnDefinition = "varchar(255)")
    private String fatherMobileNo;

    @Column(name = "father_occupation", columnDefinition = "varchar(255)")
    private String fatherOccupation;

    @Column(name = "father_email", columnDefinition = "varchar(255)")
    private String fatherEmail;

    @Column(name = "mother_name", columnDefinition = "varchar(255)")
    private String motherName;

    @Column(name = "mother_cid", columnDefinition = "varchar(255)")
    private String motherCid;

    @Column(name = "mother_mobile_no", columnDefinition = "varchar(255)")
    private String motherMobileNo;

    @Column(name = "mother_occupation", columnDefinition = "varchar(255)")
    private String motherOccupation;

    @Column(name = "mother_email", columnDefinition = "varchar(255)")
    private String motherEmail;

    @Column(name = "guardian_name", columnDefinition = "varchar(255)")
    private String guardianName;

    @Column(name = "guardian_cid", columnDefinition = "varchar(255)")
    private String guardianCid;

    @Column(name = "guardian_mobile_no", columnDefinition = "varchar(255)")
    private String guardianMobileNo;

    @Column(name = "guardian_occupation", columnDefinition = "varchar(255)")
    private String guardianOccupation;

    @Column(name = "guardian_email", columnDefinition = "varchar(255)")
    private String guardianEmail;

    @Column(name = "relation_to_guardian", columnDefinition = "varchar(255)")
    private String relationToGuardian;

    @Column(name = "social_media_link1", columnDefinition = "varchar(255)")
    private String socialMediaLink1;

    @Column(name = "social_media_link2", columnDefinition = "varchar(255)")
    private String socialMediaLink2;

    @Column(name = "social_media_link3", columnDefinition = "varchar(255)")
    private String socialMediaLink3;

    @Column(name = "profile_picture_name", columnDefinition = "varchar(255)")
    private String profilePictureName;

    @Column(name = "profile_picture_url", columnDefinition = "varchar(255)")
    private String profilePictureUrl;

    @Column(name = "profile_picture_ext", columnDefinition = "varchar(255)")
    private String profilePictureExt;

    @Column(name = "profile_picture_size", columnDefinition = "varchar(255)")
    private String profilePictureSize;

    @Column(name = "remarks", columnDefinition = "varchar(255)")
    private String remarks;

    @NotNull
    @Column(name = "signup_user", columnDefinition = "char(1)")
    private Character signupUser;
//
//    @NotNull
//    @Column(name = "status", columnDefinition = "char(1)")
//    private Character status;


    public UserInfo() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Boolean getUserLockStatus() {
        return userLockStatus;
    }

    public void setUserLockStatus(Boolean userLockStatus) {
        this.userLockStatus = userLockStatus;
    }

    public String getPresentCountry() {
        return presentCountry;
    }

    public void setPresentCountry(String presentCountry) {
        this.presentCountry = presentCountry;
    }

    public Integer getPresentDzongkhagId() {
        return presentDzongkhagId;
    }

    public void setPresentDzongkhagId(Integer presentDzongkhagId) {
        this.presentDzongkhagId = presentDzongkhagId;
    }

    public Integer getPresentGeogId() {
        return presentGeogId;
    }

    public void setPresentGeogId(Integer presentGeogId) {
        this.presentGeogId = presentGeogId;
    }

    public String getPresentPlaceName() {
        return presentPlaceName;
    }

    public void setPresentPlaceName(String presentPlaceName) {
        this.presentPlaceName = presentPlaceName;
    }

    public String getPermanentCountry() {
        return permanentCountry;
    }

    public void setPermanentCountry(String permanentCountry) {
        this.permanentCountry = permanentCountry;
    }

    public String getPermanentDzongkhag() {
        return permanentDzongkhag;
    }

    public void setPermanentDzongkhag(String permanentDzongkhag) {
        this.permanentDzongkhag = permanentDzongkhag;
    }

    public String getPermanentGeog() {
        return permanentGeog;
    }

    public void setPermanentGeog(String permanentGeog) {
        this.permanentGeog = permanentGeog;
    }

    public String getPermanentPlaceName() {
        return permanentPlaceName;
    }

    public void setPermanentPlaceName(String permanentPlaceName) {
        this.permanentPlaceName = permanentPlaceName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getFatherCid() {
        return fatherCid;
    }

    public void setFatherCid(String fatherCid) {
        this.fatherCid = fatherCid;
    }

    public String getFatherMobileNo() {
        return fatherMobileNo;
    }

    public void setFatherMobileNo(String fatherMobileNo) {
        this.fatherMobileNo = fatherMobileNo;
    }

    public String getFatherOccupation() {
        return fatherOccupation;
    }

    public void setFatherOccupation(String fatherOccupation) {
        this.fatherOccupation = fatherOccupation;
    }

    public String getFatherEmail() {
        return fatherEmail;
    }

    public void setFatherEmail(String fatherEmail) {
        this.fatherEmail = fatherEmail;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getMotherCid() {
        return motherCid;
    }

    public void setMotherCid(String motherCid) {
        this.motherCid = motherCid;
    }

    public String getMotherMobileNo() {
        return motherMobileNo;
    }

    public void setMotherMobileNo(String motherMobileNo) {
        this.motherMobileNo = motherMobileNo;
    }

    public String getMotherOccupation() {
        return motherOccupation;
    }

    public void setMotherOccupation(String motherOccupation) {
        this.motherOccupation = motherOccupation;
    }

    public String getMotherEmail() {
        return motherEmail;
    }

    public void setMotherEmail(String motherEmail) {
        this.motherEmail = motherEmail;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getGuardianCid() {
        return guardianCid;
    }

    public void setGuardianCid(String guardianCid) {
        this.guardianCid = guardianCid;
    }

    public String getGuardianMobileNo() {
        return guardianMobileNo;
    }

    public void setGuardianMobileNo(String guardianMobileNo) {
        this.guardianMobileNo = guardianMobileNo;
    }

    public String getGuardianOccupation() {
        return guardianOccupation;
    }

    public void setGuardianOccupation(String guardianOccupation) {
        this.guardianOccupation = guardianOccupation;
    }

    public String getGuardianEmail() {
        return guardianEmail;
    }

    public void setGuardianEmail(String guardianEmail) {
        this.guardianEmail = guardianEmail;
    }

    public String getRelationToGuardian() {
        return relationToGuardian;
    }

    public void setRelationToGuardian(String relationToGuardian) {
        this.relationToGuardian = relationToGuardian;
    }

    public String getSocialMediaLink1() {
        return socialMediaLink1;
    }

    public void setSocialMediaLink1(String socialMediaLink1) {
        this.socialMediaLink1 = socialMediaLink1;
    }

    public String getSocialMediaLink2() {
        return socialMediaLink2;
    }

    public void setSocialMediaLink2(String socialMediaLink2) {
        this.socialMediaLink2 = socialMediaLink2;
    }

    public String getSocialMediaLink3() {
        return socialMediaLink3;
    }

    public void setSocialMediaLink3(String socialMediaLink3) {
        this.socialMediaLink3 = socialMediaLink3;
    }

    public String getProfilePictureName() {
        return profilePictureName;
    }

    public void setProfilePictureName(String profilePictureName) {
        this.profilePictureName = profilePictureName;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getProfilePictureExt() {
        return profilePictureExt;
    }

    public void setProfilePictureExt(String profilePictureExt) {
        this.profilePictureExt = profilePictureExt;
    }

    public String getProfilePictureSize() {
        return profilePictureSize;
    }

    public void setProfilePictureSize(String profilePictureSize) {
        this.profilePictureSize = profilePictureSize;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Character getSignupUser() {
        return signupUser;
    }

    public void setSignupUser(Character signupUser) {
        this.signupUser = signupUser;
    }

}
