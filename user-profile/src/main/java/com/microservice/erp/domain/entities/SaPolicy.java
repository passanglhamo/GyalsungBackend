//package com.microservice.erp.domain.entities;
//
//import javax.persistence.*;
//import java.util.*;
//
//@Entity
//@Table(name = "sa_policies", indexes = {@Index(name = "idx_serviceName", columnList = "serviceName")})
//public class SaPolicy extends Auditable<Long, Long> {
//
//    @Column(length = 256, unique = true, nullable = false)
//    private String serviceName;
//
//    private String type;
//
//    @ManyToMany(targetEntity = SaUser.class, fetch = FetchType.LAZY)
//    private Set<SaUser> users;
//
//    @ManyToMany(targetEntity = SaRole.class, fetch = FetchType.LAZY)
//    private Set<SaRole> roles;
//
//    @OneToMany(targetEntity = SaStatement.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    private List<SaStatement> statements;
//
//    public String getServiceName() {
//        return serviceName;
//    }
//
//    public void setServiceName(String serviceName) {
//        this.serviceName = serviceName;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public Set<SaRole> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(Set<SaRole> roles) {
//        this.roles = roles;
//    }
//
//    public List<SaStatement> getStatements() {
//        return statements;
//    }
//
//    public void setStatements(List<SaStatement> statements) {
//        this.statements = statements;
//    }
//
//    public SaPolicy addRoles(SaRole... roles) {
//        if (getRoles() == null) {
//            setRoles(new HashSet<>());
//        }
//        getRoles().addAll(Arrays.asList(roles));
//        return this;
//    }
//
//    public SaPolicy addStatements(SaStatement... statements) {
//        if (getStatements() == null) {
//            setStatements(new ArrayList<>());
//        }
//        getStatements().addAll(Arrays.asList(statements));
//        return this;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        SaPolicy policy = (SaPolicy) o;
//        return getId().equals(policy.getId()) && serviceName.equals(policy.serviceName);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(getId(), serviceName);
//    }
//
//    public Set<SaUser> getUsers() {
//        return users;
//    }
//
//    public void setUsers(Set<SaUser> users) {
//        this.users = users;
//    }
//}
