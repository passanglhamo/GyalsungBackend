//package com.microservice.erp.domain.entities;
//
//import com.microservice.erp.domain.constraint.Gender.IsValidGender;
//import com.microservice.erp.domain.models.Gender;
//
//import javax.persistence.*;
//import javax.validation.constraints.Min;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Past;
//import javax.validation.constraints.Size;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Objects;
//
//@Entity(name = "SYS_USERINFO")
//@AttributeOverride(name = "id", column = @Column(name = "USERID"))
//public class UserInfo extends Auditable<Long, Long> {
//
//    @NotNull(message = "Username must not be null.")
//    @Basic(optional = false)
//    @Column(name = "USERNAME")
//    private String userName;
//
//    @NotNull(message = "Password must not be null.")
//    @Size(max = 100)
//    @Basic(optional = false)
//    @Column(name = "PASSWORD")
//    private String password;
//
//    @Basic(optional = false)
//    @NotNull(message = "Name must not be null.")
//    @Column(name = "NAME")
//    private String name;
//
//    @Column(name = "EMAIL")
//    private String email;
//
//    @IsValidGender
//    private String sex = Gender.NONE.name();
//
//    @Min(value = 18, message = "age min Value is 18.")
//    private Integer age = 18;
//
//    @NotNull(message = "dob Cannot be Null")
//    @Past(message = "Date Of Birth Must Be Greater Then Now")
//    @Basic(optional = false)
//    @Column(name = "DOB")
//    @Temporal(TemporalType.DATE)
//    private Date dob = new java.sql.Date(new Date().getTime());
//
//    @Column(name = "CID")
//    private String cid;
//
//    @Column(name = "MOBILENO")
//    private String mobileNo;
//
//    @Column(name = "EMPLOYEEID")
//    private String employeeID;
//
//    @Column(name = "DEFAULTBRANCHCODE")
//    private String defaultBranchCode;
//
//    @Column(name = "PWDCHANGEDYN")
//    private Boolean passwordChangedYN;
//
//    @Column(name = "USRFORCEPWDSTRENGTHYN")
//    private Boolean userForcePasswordStrengthYN;
//
//    @Basic(optional = false)
//    @Column(name = "USERLOCKSTATUS")
//    private Boolean userLockStatus;
//
//    @Column(name = "REMARKS")
//    private String remarks;
//
//    @NotNull
//    @Basic(optional = false)
//    @Column(name = "STATUS")
//    private String status;
//
//    public UserInfo() {
//    }
//
//    public UserInfo(@NotNull(message = "Name must not be null") String name
//            , Gender sex
//            , @Min(value = 18, message = "Min Value is 18.") int age) {
//        this();
//        this.name = name;
//        this.sex = sex.name();
//        this.age = age;
//        updateDOB(age, false);
//    }
//
//    public UserInfo(@NotNull(message = "Username must not be null.") String userName, @NotNull(message = "Password must not be null.") @Size(max = 100) String password, @NotNull(message = "Name must not be null.") String name, String email, Gender sex, @Min(value = 18, message = "age min Value is 18.") Integer age, @NotNull(message = "dob Cannot be Null") @Past(message = "Date Of Birth Must Be Greater Then Now") Date dob, String cid, String mobileNo, String employeeID, String defaultBranchCode, Boolean passwordChangedYN, Boolean userForcePasswordStrengthYN, Boolean userLockStatus, String remarks, @NotNull String status) {
//        this.userName = userName;
//        this.password = password;
//        this.name = name;
//        this.email = email;
//        this.sex = sex.name();;
//        this.age = age;
//        updateDOB(age, false);
//        this.cid = cid;
//        this.mobileNo = mobileNo;
//        this.employeeID = employeeID;
//        this.defaultBranchCode = defaultBranchCode;
//        this.passwordChangedYN = passwordChangedYN;
//        this.userForcePasswordStrengthYN = userForcePasswordStrengthYN;
//        this.userLockStatus = userLockStatus;
//        this.remarks = remarks;
//        this.status = status;
//    }
//
//    private void updateDOB(@Min(value = 18, message = "Min Value is 18.") int age, boolean isPositive) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(Objects.nonNull(getDob()) ? getDob() : new Date());
//        int year = calendar.get(Calendar.YEAR) - ((isPositive) ? -age : age);
//        calendar.set(Calendar.YEAR, year);
//        setDob(calendar.getTime());
//    }
//
//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getSex() {
//        return sex;
//    }
//
//    public void setSex(String sex) {
//        this.sex = sex;
//    }
//
//    public Integer getAge() {
//        return age;
//    }
//
//    public void setAge(Integer age) {
//        this.age = age;
//    }
//
//    public Date getDob() {
//        return dob;
//    }
//
//    public void setDob(Date dob) {
//        this.dob = dob;
//    }
//
//    public String getCid() {
//        return cid;
//    }
//
//    public void setCid(String cid) {
//        this.cid = cid;
//    }
//
//    public String getMobileNo() {
//        return mobileNo;
//    }
//
//    public void setMobileNo(String mobileNo) {
//        this.mobileNo = mobileNo;
//    }
//
//    public String getEmployeeID() {
//        return employeeID;
//    }
//
//    public void setEmployeeID(String employeeID) {
//        this.employeeID = employeeID;
//    }
//
//    public String getDefaultBranchCode() {
//        return defaultBranchCode;
//    }
//
//    public void setDefaultBranchCode(String defaultBranchCode) {
//        this.defaultBranchCode = defaultBranchCode;
//    }
//
//    public Boolean getPasswordChangedYN() {
//        return passwordChangedYN;
//    }
//
//    public void setPasswordChangedYN(Boolean passwordChangedYN) {
//        this.passwordChangedYN = passwordChangedYN;
//    }
//
//    public Boolean getUserForcePasswordStrengthYN() {
//        return userForcePasswordStrengthYN;
//    }
//
//    public void setUserForcePasswordStrengthYN(Boolean userForcePasswordStrengthYN) {
//        this.userForcePasswordStrengthYN = userForcePasswordStrengthYN;
//    }
//
//    public Boolean getUserLockStatus() {
//        return userLockStatus;
//    }
//
//    public void setUserLockStatus(Boolean userLockStatus) {
//        this.userLockStatus = userLockStatus;
//    }
//
//    public String getRemarks() {
//        return remarks;
//    }
//
//    public void setRemarks(String remarks) {
//        this.remarks = remarks;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String statusId) {
//        this.status = statusId;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        UserInfo userInfo = (UserInfo) o;
//        return Objects.equals(getId(), userInfo.getId());
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(getId());
//    }
//}
