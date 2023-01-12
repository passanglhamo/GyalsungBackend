package com.microservice.erp.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.*;

@Entity
//@Table(name="policies", indexes = {@Index(name = "idx_serviceName",columnList = "serviceName")})
@Table(name="policies")
@AttributeOverride(name = "id", column = @Column(name = "policy_id", columnDefinition = "bigint"))
public class Policy extends Auditable<BigInteger, Long> {

    @Column(length = 250,  nullable = false)
    private String policyName;

    private String type;

    @ManyToMany(targetEntity = User.class, fetch = FetchType.LAZY, mappedBy = "policies")
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    @ManyToMany(targetEntity = Role.class, fetch = FetchType.LAZY, mappedBy = "policies")
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();

    @OneToMany(targetEntity = Statement.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_policies")
    private List<Statement> statements;

    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public void setStatements(List<Statement> statements) {
        this.statements = statements;
    }

    /*public Policy addRoles(Role...roles){
        if (getRoles() == null){
            setRoles(new HashSet<>());
        }
        getRoles().addAll(Arrays.asList(roles));
        return this;
    }*/

    public Policy addStatements(Statement...statements){
        if (getStatements() == null){
            setStatements(new ArrayList<>());
        }
        getStatements().addAll(Arrays.asList(statements));
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Policy policy = (Policy) o;
        return getId().equals(policy.getId()) && policyName.equals(policy.policyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), policyName);
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
