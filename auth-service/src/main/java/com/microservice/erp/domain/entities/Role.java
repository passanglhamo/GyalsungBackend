package com.microservice.erp.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@Entity
@Table(name="roles", indexes = {@Index(name = "idx_role_name",columnList = "role_name")})
@AttributeOverride(name = "id", column = @Column(name = "role_id", columnDefinition = "bigint"))
public class Role extends Auditable<BigInteger, Long> {

    @Column(name = "role_name", columnDefinition = "varchar(255)")
    private String roleName;

    @Column(name = "user_type", columnDefinition = "char(1)")
    private Character userType;


    @ManyToMany(targetEntity = User.class, fetch = FetchType.LAZY, mappedBy = "roles",cascade = CascadeType.DETACH)
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    @ManyToMany(targetEntity = Policy.class, fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JsonIgnore
    private Set<Policy> policies;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Character getUserType() {
        return userType;
    }

    public void setUserType(Character userType) {
        this.userType = userType;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Policy> getPolicies() {
        return policies;
    }

    public void setPolicies(Set<Policy> policies) {
        this.policies = policies;
    }

//    public Role addUsers(User...users){
//        if (getUsers() == null){
//            setUsers(new HashSet<>());
//        }
//        getUsers().addAll(Arrays.asList(users));
//        return this;
//    }


    public Role addPolicies(Policy...policies){
        if (getPolicies() == null){
            setUsers(new HashSet<>());
        }
        getPolicies().addAll(Arrays.asList(policies));
        return this;
    }
//    public Role addPolicies(Policy...policies){
//        if (getPolicies() == null){
//            setPolicies(new HashSet<>());
//        }
//        //getPolicies().addAll(Arrays.asList(policies));
//        Stream.of(policies)
//                .forEach(policy -> {
//                    policy.getRoles().add(this);
//                    this.addPolicies(policy);
//                });
//        return this;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return getId().equals(role.getId()) && roleName.equals(role.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), roleName);
    }
}
