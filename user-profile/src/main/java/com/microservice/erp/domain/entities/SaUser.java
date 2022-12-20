package com.microservice.erp.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigInteger;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Setter
@Getter
@AllArgsConstructor
@Entity(name = "sys_userinfo")
@AttributeOverride(name = "id", column = @Column(name = "user_id", columnDefinition = "bigint"))
public class SaUser extends Auditable<BigInteger, Long> implements UserDetails {

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
    @Column(name = "signup_user")
    private Character signupUser;

    @NotNull
    @Column(name = "status")
    private Character status;

    @ManyToMany(targetEntity = SaRole.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<SaRole> roles;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "sa_users_secrets")
    @Column(name = "secrets")
    @JsonIgnore
    private Map<Integer, String> secrets;


    private boolean enabled;

    public static Map<Integer, String> createRandomMapOfSecret() {
        Map<Integer, String> secrets = new HashMap<>();
        secrets.put(1110, UUID.randomUUID().toString());
        secrets.put(1111, UUID.randomUUID().toString());
        secrets.put(1112, UUID.randomUUID().toString());
        secrets.put(1113, UUID.randomUUID().toString());
        secrets.put(1114, UUID.randomUUID().toString());
        return secrets;
    }

    public SaUser addRoles(SaRole... roles) {
        if (getRoles() == null) {
            setRoles(new HashSet<>());
        }
        getRoles().addAll(Arrays.asList(roles));
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaUser user = (SaUser) o;
        return getId().equals(user.getId()) && username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), username);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null) return new ArrayList<>();
        return this.roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(toList());
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }


    public SaUser() {

    }
}
