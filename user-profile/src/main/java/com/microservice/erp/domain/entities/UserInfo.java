package com.microservice.erp.domain.entities;

import com.microservice.erp.domain.constraint.Gender.IsValidGender;
import com.microservice.erp.domain.models.Gender;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.*;

/**
 * @author Rajib Kumer Ghosh
 */

@Entity(name = "SYS_USERINFO")
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
public class UserInfo extends Auditable<Long, Long> {

    @NotNull(message = "Username must not be null.")
    @Basic(optional = false)
    @Column(name = "username", columnDefinition = "varchar(255)")
    private String username;

    @NotNull(message = "Password cannot be null")
    @Size(max = 250)
    @Basic(optional = false)
    @Column(name = "password", columnDefinition = "varchar(255)")
    private String password;

    @Basic(optional = false)
    @NotNull(message = "Full name cannot be null")
    @Column(name = "full_name", columnDefinition = "varchar(255)")
    private String fullName;

    @Column(name = "email", columnDefinition = "varchar(255)")
    private String email;

    @IsValidGender
    private String sex = Gender.NONE.name();

    @Min(value = 18, message = "Minimum age value must be 18")
    private Integer age = 18;

    @NotNull(message = "dob Cannot be Null")
    @Past(message = "Date Of Birth Must Be Greater Then Now")
    @Basic(optional = false)
    @Column(name = "DOB")
    @Temporal(TemporalType.DATE)
    private Date dob = new java.sql.Date(new Date().getTime());

    @NotNull(message = "CID cannot be null")
    @Basic(optional = false)
    @Column(name = "cid", columnDefinition = "varchar(255)")
    private String cid;

    @NotNull(message = "Mobile no cannot be null")
    @Basic(optional = false)
    @Column(name = "mobile_no", columnDefinition = "varchar(255)")
    private String mobileNo;

    @Column(name = "employee_id", columnDefinition = "varchar(255)")
    private String employeeId;

    @Column(name = "default_branch_code", columnDefinition = "varchar(255)")
    private String defaultBranchCode;

    @Column(name = "pwd_change_yn", columnDefinition = "boolean")
    private Boolean passwordChangedYN;

    @Column(name = "user_force_password_strength_yn", columnDefinition = "boolean")
    private Boolean userForcePasswordStrengthYN;

    @Column(name = "user_lock_status", columnDefinition = "boolean")
    private Boolean userLockStatus;

    @Column(name = "present_country", columnDefinition = "varchar(255)")
    private String presentCountry;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "present_dzongkhag_id", referencedColumnName = "dzongkhag_id")
    private Dzongkhag presentDzongkhag;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "present_geog_id", referencedColumnName = "geog_id")
    private Geog presentGeog;

    @Column(name = "present_palce_name", columnDefinition = "varchar(255)")
    private String presentPlaceName;

    @Column(name = "permanent_country", columnDefinition = "varchar(255)")
    private String permanentCountry;

    @Column(name = "permanent_dzongkhag", columnDefinition = "varchar(255)")
    private String permanentDzongkhag;

    @Column(name = "permanent_geog", columnDefinition = "varchar(255)")
    private String permanentGeog;

    @Column(name = "permanent_pace_name", columnDefinition = "varchar(255)")
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

    @Column(name = "guargian_occupation", columnDefinition = "varchar(255)")
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

//    @Column(name = "profile_url")
//    private String pictureUrl;

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
    //@Basic(optional = false)
    @Column(name = "status")
    private Character status;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sa_user_roles",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId"))
    private Set<Role> roles = new HashSet<>();

    public UserInfo() {
    }

    public UserInfo(@NotNull(message = "Name must not be null") String fullName
            , Gender sex
            , @Min(value = 18, message = "Min Value is 18.") int age) {
        this();
        this.fullName = fullName;
        this.sex = sex.name();
        this.age = age;
        updateDOB(age, false);
    }

    public UserInfo(
            @NotNull(message = "Password must not be null.") @Size(max = 100) String password,
            @NotNull(message = "Name must not be null.") String fullName, String email, String sex,
            @Min(value = 18, message = "age min Value is 18.") Integer age, @NotNull(message = "dob Cannot be Null")
            @Past(message = "Date Of Birth Must Be Greater Then Now") Date dob,
            @NotNull(message = "CID must not be null.") String cid,
            @NotNull(message = "mobileNo must not be null.") String mobileNo,
            String employeeId, String defaultBranchCode, Boolean passwordChangedYN,
            Boolean userForcePasswordStrengthYN, Boolean userLockStatus, String presentCountry,
            String presentPlaceName,
            String permanentCountry, String permanentDzongkhag, String permanentGeog,
            String permanentPlaceName, String fatherName, String fatherCid, String fatherMobileNo,
            String fatherOccupation, String fatherEmail, String motherName, String motherCid,
            String motherMobileNo, String motherOccupation, String motherEmail,
            String socialMediaLink1,
            String socialMediaLink2, String socialMediaLink3, String pictureUrl, String remarks,
            @NotNull Character status) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.sex = sex;
        this.age = age;
        updateDOB(age, false);
        this.cid = cid;
        this.mobileNo = mobileNo;
        this.employeeId = employeeId;
        this.defaultBranchCode = defaultBranchCode;
        this.passwordChangedYN = passwordChangedYN;
        this.userForcePasswordStrengthYN = userForcePasswordStrengthYN;
        this.userLockStatus = userLockStatus;
        this.presentCountry = presentCountry;
        this.presentPlaceName = presentPlaceName;
        this.permanentCountry = permanentCountry;
        this.permanentDzongkhag = permanentDzongkhag;
        this.permanentGeog = permanentGeog;
        this.permanentPlaceName = permanentPlaceName;
        this.fatherName = fatherName;
        this.fatherCid = fatherCid;
        this.fatherMobileNo = fatherMobileNo;
        this.fatherOccupation = fatherOccupation;
        this.fatherEmail = fatherEmail;
        this.motherName = motherName;
        this.motherCid = motherCid;
        this.motherMobileNo = motherMobileNo;
        this.motherOccupation = motherOccupation;
        this.motherEmail = motherEmail;
        this.socialMediaLink1 = socialMediaLink1;
        this.socialMediaLink2 = socialMediaLink2;
        this.socialMediaLink3 = socialMediaLink3;
        this.profilePictureUrl = pictureUrl;
        this.remarks = remarks;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDefaultBranchCode() {
        return defaultBranchCode;
    }

    public void setDefaultBranchCode(String defaultBranchCode) {
        this.defaultBranchCode = defaultBranchCode;
    }

    public Boolean getPasswordChangedYN() {
        return passwordChangedYN;
    }

    public void setPasswordChangedYN(Boolean passwordChangedYN) {
        this.passwordChangedYN = passwordChangedYN;
    }

    public Boolean getUserForcePasswordStrengthYN() {
        return userForcePasswordStrengthYN;
    }

    public void setUserForcePasswordStrengthYN(Boolean userForcePasswordStrengthYN) {
        this.userForcePasswordStrengthYN = userForcePasswordStrengthYN;
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

    public Dzongkhag getPresentDzongkhag() {
        return presentDzongkhag;
    }

    public void setPresentDzongkhag(Dzongkhag presentDzongkhag) {
        this.presentDzongkhag = presentDzongkhag;
    }

    public Geog getPresentGeog() {
        return presentGeog;
    }

    public void setPresentGeog(Geog presentGeog) {
        this.presentGeog = presentGeog;
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

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    private void updateDOB(@Min(value = 18, message = "Min age value must be 18") int age, boolean isPositive) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Objects.nonNull(getDob()) ? getDob() : new Date());
        int year = calendar.get(Calendar.YEAR) - ((isPositive) ? -age : age);
        calendar.set(Calendar.YEAR, year);
        setDob(calendar.getTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfo userInfo = (UserInfo) o;
        return Objects.equals(getId(), userInfo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
