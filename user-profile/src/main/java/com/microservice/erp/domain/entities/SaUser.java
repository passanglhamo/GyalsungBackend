package com.microservice.erp.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@Entity(name = "sys_userinfo")
@AttributeOverride(name = "id", column = @Column(name = "user_id", columnDefinition = "bigint"))
public class SaUser extends Auditable<BigInteger, Long> {

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

    @NotNull(message = "Email cannot be null")
    @Column(name = "email", columnDefinition = "varchar(255)")
    private String email;

    @Column(name = "gender", columnDefinition = "char(1)")
    private Character gender;

    //    @NotNull(message = "DOB cannot be null")
//    @Past(message = "DOB must be greater than now")
//    @Basic(optional = false)
    @Column(name = "dob")
    private Date dob;

    //    @NotNull(message = "CID cannot be null")
//    @Basic(optional = false)
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
    @Column(name = "signup_user")
    private Character signupUser;

    @NotNull
    @Column(name = "status")
    private Character status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sa_user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<SaRole> saRoles = new HashSet<>();

    public SaUser() {

    }
}