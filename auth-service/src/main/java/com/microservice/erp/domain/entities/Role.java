package com.microservice.erp.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="roles", indexes = {@Index(name = "idx_name",columnList = "name")})
public class Role extends Auditable<Long, Long> {

    @Column(length = 250, unique = true, nullable = false)
    private String name;

    @ManyToMany(targetEntity = User.class, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<User> users;

    @ManyToMany(targetEntity = Policy.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Policy> policies;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Role addUsers(User...users){
        if (getUsers() == null){
            setUsers(new HashSet<>());
        }
        getUsers().addAll(Arrays.asList(users));
        return this;
    }

    public Role addPolicies(Policy...policies){
        if (getPolicies() == null){
            setPolicies(new HashSet<>());
        }
        getPolicies().addAll(Arrays.asList(policies));
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return getId().equals(role.getId()) && name.equals(role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), name);
    }
}
