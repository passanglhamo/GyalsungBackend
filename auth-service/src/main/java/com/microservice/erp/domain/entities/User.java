package com.microservice.erp.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
@Table(name="users", indexes = {@Index(name = "idx_username",columnList = "username")
        , @Index(name = "idx_email", columnList = "email")})
public class User extends Auditable<Long, Long> implements UserDetails {

    @Column(length = 250, unique = true, nullable = false)
    private String username;

    @Column(nullable = false) @JsonIgnore
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "users_secrets")
    @Column(name = "secrets") @JsonIgnore
    private Map<Integer, String> secrets;

    @Column(length = 200, unique = true)
    private String email;

    @Column(length = 200, unique = true)
    private String mobile;

    private String firstName;
    private String lastName;
    private boolean enabled;

    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Role> roles;

    @ManyToMany(targetEntity = Policy.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Policy> policies;

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

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Map<Integer, String> getSecrets() {
        return secrets;
    }

    public void setSecrets(Map<Integer, String> secrets) {
        this.secrets = secrets;
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

    public User addRoles(Role...roles){
        if (getRoles() == null){
            setRoles(new HashSet<>());
        }
        getRoles().addAll(Arrays.asList(roles));
        return this;
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

    public User addPolicies(Policy...policies){
        if (getPolicies() == null){
            setPolicies(new HashSet<>());
        }
        getPolicies().addAll(Arrays.asList(policies));
        return this;
    }
}
