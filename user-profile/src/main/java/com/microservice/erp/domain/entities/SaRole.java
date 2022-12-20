package com.microservice.erp.domain.entities;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "roles", indexes = {@Index(name = "idx_name", columnList = "name")})
public class SaRole extends Auditable<BigInteger, Long> {

    @Column(length = 256, unique = true, nullable = false)
    private String name;

    @ManyToMany(targetEntity = SaUser.class, fetch = FetchType.LAZY)
    private Set<SaUser> users;

//    @ManyToMany(targetEntity = SaPolicy.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    private Set<SaPolicy> policies;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<SaUser> getUsers() {
        return users;
    }

    public void setUsers(Set<SaUser> users) {
        this.users = users;
    }

//    public Set<SaPolicy> getPolicies() {
//        return policies;
//    }
//
//    public void setPolicies(Set<SaPolicy> policies) {
//        this.policies = policies;
//    }

    public SaRole addUsers(SaUser... users) {
        if (getUsers() == null) {
            setUsers(new HashSet<>());
        }
        getUsers().addAll(Arrays.asList(users));
        return this;
    }

//    public SaRole addPolicies(SaPolicy... policies) {
//        if (getPolicies() == null) {
//            setPolicies(new HashSet<>());
//        }
//        getPolicies().addAll(Arrays.asList(policies));
//        return this;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaRole role = (SaRole) o;
        return getId().equals(role.getId()) && name.equals(role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), name);
    }
}
