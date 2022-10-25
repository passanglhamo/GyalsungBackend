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
@Setter
@Getter
@AllArgsConstructor
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

    @NotNull(message = "DOB cannot be null")
    @Past(message = "DOB must be greater than now")
    @Basic(optional = false)
    @Column(name = "dob")
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

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "present_dzongkhag_id", referencedColumnName = "dzongkhag_id")
//    private Dzongkhag presentDzongkhag;
//
    @Column(name = "present_dzongkhag_id", columnDefinition = "int")
    private Integer presentDzongkhagId;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "present_geog_id", referencedColumnName = "geog_id")
//    private Geog presentGeog;

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

//
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "sa_user_roles",
//            joinColumns = @JoinColumn(name = "userId"),
//            inverseJoinColumns = @JoinColumn(name = "roleId"))
//    private Set<Role> roles = new HashSet<>();

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
