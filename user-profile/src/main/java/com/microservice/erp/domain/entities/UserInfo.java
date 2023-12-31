package com.microservice.erp.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.microservice.erp.domain.helper.BaseEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;


@AllArgsConstructor
@Entity(name = "user_info")
//@AttributeOverride(name = "id", column = @Column(name = "user_id", columnDefinition = "bigint"))
//public class UserInfo extends Auditable<BigInteger, Long> implements UserDetails {
public class UserInfo extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", columnDefinition = "bigint")
    private BigInteger userId;

    @NotNull(message = "Username must not be null.")
    @Basic(optional = false)
    @Column(name = "username", columnDefinition = "varchar(255)")
    private String username;

    @Basic(optional = false)
    @NotNull(message = "Full name cannot be null")
    @Column(name = "full_name", columnDefinition = "varchar(255)")
    private String fullName;

    //    @NotNull(message = "Email cannot be null")
    @Column(name = "email", columnDefinition = "varchar(255)")
    private String email;

    @Column(name = "gender", columnDefinition = "char(1)")
    private Character gender;

    @Column(name = "dob")
    private Date dob;

    @Column(name = "cid", columnDefinition = "varchar(255)")
    private String cid;

    //    @NotNull(message = "Mobile no cannot be null")
//    @Basic(optional = false)
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

    @Column(name = "guardian_name_first", columnDefinition = "varchar(255)")
    private String guardianNameFirst;

    @Column(name = "guardian_cid_first", columnDefinition = "varchar(255)")
    private String guardianCidFirst;

    @Column(name = "guardian_mobile_no_first", columnDefinition = "varchar(255)")
    private String guardianMobileNoFirst;

    @Column(name = "guardian_occupation_first", columnDefinition = "varchar(255)")
    private String guardianOccupationFirst;

    @Column(name = "guardian_email_first", columnDefinition = "varchar(255)")
    private String guardianEmailFirst;

    @Column(name = "relation_to_guardian_first", columnDefinition = "varchar(255)")
    private String relationToGuardianFirst;

    @Column(name = "guardian_name_second", columnDefinition = "varchar(255)")
    private String guardianNameSecond;

    @Column(name = "guardian_cid_second", columnDefinition = "varchar(255)")
    private String guardianCidSecond;

    @Column(name = "guardian_mobile_no_second", columnDefinition = "varchar(255)")
    private String guardianMobileNoSecond;

    @Column(name = "guardian_occupation_second", columnDefinition = "varchar(255)")
    private String guardianOccupationSecond;

    @Column(name = "guardian_email_second", columnDefinition = "varchar(255)")
    private String guardianEmailSecond;

    @Column(name = "relation_to_guardian_second", columnDefinition = "varchar(255)")
    private String relationToGuardianSecond;

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

    @Column(name = "helper_required", columnDefinition = "char(1)")
    private Character helperRequired;

    @Column(name = "helper_name", columnDefinition = "varchar(255)")
    private String helperName;

    @Column(name = "helper_mobile_no", columnDefinition = "varchar(255)")
    private String helperMobileNo;

    @Column(name = "helper_email", columnDefinition = "varchar(255)")
    private String helperEmail;

    @Column(name = "helper_relation", columnDefinition = "varchar(255)")
    private String helperRelation;

    @JsonIgnore
    private boolean enabled;


    public UserInfo() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    @JsonIgnore
    @Transient
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    @JsonIgnore
    @Transient
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    @JsonIgnore
    @Transient
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Character getHelperRequired() {
        return helperRequired;
    }

    public void setHelperRequired(Character helperRequired) {
        this.helperRequired = helperRequired;
    }

    public String getHelperName() {
        return helperName;
    }

    public void setHelperName(String helperName) {
        this.helperName = helperName;
    }

    public String getHelperMobileNo() {
        return helperMobileNo;
    }

    public void setHelperMobileNo(String helperMobileNo) {
        this.helperMobileNo = helperMobileNo;
    }

    public String getHelperEmail() {
        return helperEmail;
    }

    public void setHelperEmail(String helperEmail) {
        this.helperEmail = helperEmail;
    }

    public String getHelperRelation() {
        return helperRelation;
    }

    public void setHelperRelation(String helperRelation) {
        this.helperRelation = helperRelation;
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

    public String getGuardianNameFirst() {
        return guardianNameFirst;
    }

    public void setGuardianNameFirst(String guardianNameFirst) {
        this.guardianNameFirst = guardianNameFirst;
    }

    public String getGuardianCidFirst() {
        return guardianCidFirst;
    }

    public void setGuardianCidFirst(String guardianCidFirst) {
        this.guardianCidFirst = guardianCidFirst;
    }

    public String getGuardianMobileNoFirst() {
        return guardianMobileNoFirst;
    }

    public void setGuardianMobileNoFirst(String guardianMobileNoFirst) {
        this.guardianMobileNoFirst = guardianMobileNoFirst;
    }

    public String getGuardianOccupationFirst() {
        return guardianOccupationFirst;
    }

    public void setGuardianOccupationFirst(String guardianOccupationFirst) {
        this.guardianOccupationFirst = guardianOccupationFirst;
    }

    public String getGuardianEmailFirst() {
        return guardianEmailFirst;
    }

    public void setGuardianEmailFirst(String guardianEmailFirst) {
        this.guardianEmailFirst = guardianEmailFirst;
    }

    public String getRelationToGuardianFirst() {
        return relationToGuardianFirst;
    }

    public void setRelationToGuardianFirst(String relationToGuardianFirst) {
        this.relationToGuardianFirst = relationToGuardianFirst;
    }

    public String getGuardianNameSecond() {
        return guardianNameSecond;
    }

    public void setGuardianNameSecond(String guardianNameSecond) {
        this.guardianNameSecond = guardianNameSecond;
    }

    public String getGuardianCidSecond() {
        return guardianCidSecond;
    }

    public void setGuardianCidSecond(String guardianCidSecond) {
        this.guardianCidSecond = guardianCidSecond;
    }

    public String getGuardianMobileNoSecond() {
        return guardianMobileNoSecond;
    }

    public void setGuardianMobileNoSecond(String guardianMobileNoSecond) {
        this.guardianMobileNoSecond = guardianMobileNoSecond;
    }

    public String getGuardianOccupationSecond() {
        return guardianOccupationSecond;
    }

    public void setGuardianOccupationSecond(String guardianOccupationSecond) {
        this.guardianOccupationSecond = guardianOccupationSecond;
    }

    public String getGuardianEmailSecond() {
        return guardianEmailSecond;
    }

    public void setGuardianEmailSecond(String guardianEmailSecond) {
        this.guardianEmailSecond = guardianEmailSecond;
    }

    public String getRelationToGuardianSecond() {
        return relationToGuardianSecond;
    }

    public void setRelationToGuardianSecond(String relationToGuardianSecond) {
        this.relationToGuardianSecond = relationToGuardianSecond;
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

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        UserInfo user = (UserInfo) o;
//        return getId().equals(user.getId()) && username.equals(user.username);
//    }
//
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(getId(), username);
//    }

}
