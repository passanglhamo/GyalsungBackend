package com.microservice.erp.domain.entities;

import com.microservice.erp.domain.constraint.Gender.IsValidGender;
import com.microservice.erp.domain.models.Gender;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@Entity(name = "SYS_USERINFO_A")
@AttributeOverride(name = "id", column = @Column(name = "AUDIT_SERIAL_NO"))
public class UserInfo_A extends Auditable<Long, Long> {

    /*@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "AUDIT_SERIAL_NO")
    private Integer auditSerialNo;*/

    @NotNull(message = "UserID must not be null.")
    @Basic(optional = false)
    @Column(name = "USERID")
    private String userID;

    @NotNull(message = "Username must not be null.")
    @Basic(optional = false)
    @Column(name = "USERNAME")
    private String userName;

    @NotNull(message = "Password must not be null.")
    @Size(max = 100)
    @Basic(optional = false)
    @Column(name = "PASSWORD")
    private String password;

    @Basic(optional = false)
    @NotNull(message = "Name must not be null.")
    @Column(name = "NAME")
    private String name;

    @Column(name = "EMAIL")
    private String email;

    @IsValidGender
    private String sex = Gender.NONE.name();

    @Min(value = 18, message = "age min Value is 18.")
    private Integer age = 18;

    @NotNull(message = "dob Cannot be Null")
    @Past(message = "Date Of Birth Must Be Greater Then Now")
    @Basic(optional = false)
    @Column(name = "DOB")
    @Temporal(TemporalType.DATE)
    private Date dob = new java.sql.Date(new Date().getTime());

    @NotNull(message = "CID must not be null.")
    @Basic(optional = false)
    @Column(name = "CID")
    private String cid;

    @NotNull(message = "mobileNo must not be null.")
    @Basic(optional = false)
    @Column(name = "MOBILENO")
    private String mobileNo;

    @Column(name = "EMPLOYEEID")
    private String employeeID;

    @Column(name = "DEFAULT_BRANCH_CODE")
    private String defaultBranchCode;

    @Column(name = "PWD_CHANGED_YN")
    private Boolean passwordChangedYN;

    @Column(name = "USR_FORCE_PWD_STRENGTH_YN")
    private Boolean userForcePasswordStrengthYN;

    @Basic(optional = false)
    @Column(name = "USER_LOCK_STATUS")
    private Boolean userLockStatus;

    @Column(name = "PRESENT_COUNTRY")
    private String presentCountry;

    @Column(name = "PRESENT_DZONGKHAG")
    private String presentDzongkhag;

    @Column(name = "PRESENT_GEWOG")
    private String presentGewog;

    @Column(name = "PRESENT_PLACE_NAME")
    private String presentPlaceName;

    @Column(name = "PERMANENT_COUNTRY")
    private String permanentCountry;

    @Column(name = "PERMANENT_DZONGKHAG")
    private String permanentDzongkhag;

    @Column(name = "PERMANENT_GEWOG")
    private String permanentGewog;

    @Column(name = "PERMANENT_PLACE_NAME")
    private String permanentPlaceName;

    @Column(name = "FATHER_NAME")
    private String fatherName;

    @Column(name = "FATHER_CID")
    private String fatherCid;

    @Column(name = "FATHER_MOBILE_NO")
    private String fatherMobileNo;

    @Column(name = "FATHER_OCCUPATION")
    private String fatherOccupation;

    @Column(name = "FATHER_EMAIL")
    private String fatherEmail;

    @Column(name = "MOTHER_NAME")
    private String motherName;

    @Column(name = "MOTHER_CID")
    private String motherCid;

    @Column(name = "MOTHER_MOBILE_NO")
    private String motherMobileNo;

    @Column(name = "MOTHER_OCCUPATION")
    private String motherOccupation;

    @Column(name = "MOTHER_EMAIL")
    private String motherEmail;

    @NotNull(message = "mobileNo must not be null.")
    @Basic(optional = false)
    @Column(name = "GUARDIAN_NAME")
    private String guardianName;

    @NotNull(message = "mobileNo must not be null.")
    @Basic(optional = false)
    @Column(name = "GUARDIAN_CID")
    private String guardianCid;

    @Column(name = "GUARDIAN_MOBILE_NO")
    private String guardianMobileNo;

    @Column(name = "SOCIAL_MEDIA_LINK1")
    private String socialMediaLink1;

    @Column(name = "SOCIAL_MEDIA_LINK2")
    private String socialMediaLink2;

    @Column(name = "SOCIAL_MEDIA_LINK3")
    private String socialMediaLink3;

    @Column(name = "PICTURE_URL")
    private String pictureUrl;

    @Column(name = "REMARKS")
    private String remarks;

    @NotNull
    @Basic(optional = false)
    @Column(name = "STATUS")
    private String status;

    @NotNull
    @Basic(optional = false)
    @Column(name = "CMD_FLAG")
    private String cmdFlag;

    public UserInfo_A() {
    }

    public UserInfo_A(@NotNull(message = "UserID must not be null.") String userID, @NotNull(message = "Username must not be null.") String userName, @NotNull(message = "Password must not be null.") @Size(max = 100) String password, @NotNull(message = "Name must not be null.") String name, String email, String sex, @Min(value = 18, message = "age min Value is 18.") Integer age, @NotNull(message = "dob Cannot be Null") @Past(message = "Date Of Birth Must Be Greater Then Now") Date dob, @NotNull(message = "CID must not be null.") String cid, @NotNull(message = "mobileNo must not be null.") String mobileNo, String employeeID, String defaultBranchCode, Boolean passwordChangedYN, Boolean userForcePasswordStrengthYN, Boolean userLockStatus, String presentCountry, String presentDzongkhag, String presentGewog, String presentPlaceName, String permanentCountry, String permanentDzongkhag, String permanentGewog, String permanentPlaceName, String fatherName, String fatherCid, String fatherMobileNo, String fatherOccupation, String fatherEmail, String motherName, String motherCid, String motherMobileNo, String motherOccupation, String motherEmail, @NotNull(message = "mobileNo must not be null.") String guardianName, @NotNull(message = "mobileNo must not be null.") String guardianCid, String guardianMobileNo, String socialMediaLink1, String socialMediaLink2, String socialMediaLink3, String pictureUrl, String remarks, @NotNull String status, @NotNull String cmdFlag) {
        //this.auditSerialNo = auditSerialNo;
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.email = email;
        this.sex = sex;
        this.age = age;
        updateDOB(age, false);
        this.cid = cid;
        this.mobileNo = mobileNo;
        this.employeeID = employeeID;
        this.defaultBranchCode = defaultBranchCode;
        this.passwordChangedYN = passwordChangedYN;
        this.userForcePasswordStrengthYN = userForcePasswordStrengthYN;
        this.userLockStatus = userLockStatus;
        this.presentCountry = presentCountry;
        this.presentDzongkhag = presentDzongkhag;
        this.presentGewog = presentGewog;
        this.presentPlaceName = presentPlaceName;
        this.permanentCountry = permanentCountry;
        this.permanentDzongkhag = permanentDzongkhag;
        this.permanentGewog = permanentGewog;
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
        this.guardianName = guardianName;
        this.guardianCid = guardianCid;
        this.guardianMobileNo = guardianMobileNo;
        this.socialMediaLink1 = socialMediaLink1;
        this.socialMediaLink2 = socialMediaLink2;
        this.socialMediaLink3 = socialMediaLink3;
        this.pictureUrl = pictureUrl;
        this.remarks = remarks;
        this.status = status;
        this.cmdFlag = cmdFlag;
    }

    private void updateDOB(@Min(value = 18, message = "Min Value is 18.") int age, boolean isPositive) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Objects.nonNull(getDob()) ? getDob() : new Date());
        int year = calendar.get(Calendar.YEAR) - ((isPositive) ? -age : age);
        calendar.set(Calendar.YEAR, year);
        setDob(calendar.getTime());
    }

    /*public Integer getAuditSerialNo() {
        return auditSerialNo;
    }

    public void setAuditSerialNo(Integer auditSerialNo) {
        this.auditSerialNo = auditSerialNo;
    }*/

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
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

    public String getPresentDzongkhag() {
        return presentDzongkhag;
    }

    public void setPresentDzongkhag(String presentDzongkhag) {
        this.presentDzongkhag = presentDzongkhag;
    }

    public String getPresentGewog() {
        return presentGewog;
    }

    public void setPresentGewog(String presentGewog) {
        this.presentGewog = presentGewog;
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

    public String getPermanentGewog() {
        return permanentGewog;
    }

    public void setPermanentGewog(String permanentGewog) {
        this.permanentGewog = permanentGewog;
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

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCmdFlag() {
        return cmdFlag;
    }

    public void setCmdFlag(String cmdFlag) {
        this.cmdFlag = cmdFlag;
    }
}
