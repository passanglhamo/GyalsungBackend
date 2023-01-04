package com.microservice.erp.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
@Table(name = "users", indexes = {@Index(name = "idx_username", columnList = "username")
        , @Index(name = "idx_email", columnList = "email")})
@AttributeOverride(name = "id", column = @Column(name = "id", columnDefinition = "bigint"))
public class User extends Auditable<BigInteger, Long> implements UserDetails {
//todo:Added user id for time being
    @Column(name = "user_id",  columnDefinition = "bigint")
    private BigInteger userId;

    @Column(length = 250, unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;


    @Column(length = 200, unique = true)
    private String email;

    @Column(length = 200, unique = true)
    private String cid;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "users_secrets")
    @Column(name = "secrets")
    @JsonIgnore
    private Map<Integer, String> secrets;


    @JsonIgnore
    private boolean enabled;
//
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "sa_user_role_mapping",
//            joinColumns = @JoinColumn(name = "auth_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id"))
//    private Set<Role> roles = new HashSet<>();

    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Role> roles;

    @ManyToMany(targetEntity = Policy.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Policy> policies;

    @Override
    @JsonIgnore
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null) return new ArrayList<>();
        return this.roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(toList());
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
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

    public void setUsername(String username) {
        this.username = username;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


    public Map<Integer, String> getSecrets() {
        return secrets;
    }

    public void setSecrets(Map<Integer, String> secrets) {
        this.secrets = secrets;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public static Map<Integer, String> createRandomMapOfSecret() {
        Map<Integer, String> secrets = new HashMap<>();
        secrets.put(1110, UUID.randomUUID().toString());
        secrets.put(1111, UUID.randomUUID().toString());
        secrets.put(1112, UUID.randomUUID().toString());
        secrets.put(1113, UUID.randomUUID().toString());
        secrets.put(1114, UUID.randomUUID().toString());
        return secrets;
    }

    public void addRoles(Role... roles) {
        if (getRoles() == null) {
            setRoles(new HashSet<>());
        }
        getRoles().addAll(Arrays.asList(roles));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getId().equals(user.getId()) && username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), username);
    }

    public Set<Policy> getPolicies() {
        return policies;
    }

    public void setPolicies(Set<Policy> policies) {
        this.policies = policies;
    }

    public User addPolicies(Policy... policies) {
        if (getPolicies() == null) {
            setPolicies(new HashSet<>());
        }
        getPolicies().addAll(Arrays.asList(policies));
        return this;
    }
}
