//package com.microservice.erp.domain.entities;
//
//import javax.persistence.*;
//import java.util.*;
//
//@Entity
//@Table(name="policies", indexes = {@Index(name = "idx_serviceName",columnList = "serviceName")})
//public class Policy extends Auditable<Long, Long> {
//
//    @Column(length = 256, unique = true, nullable = false)
//    private String serviceName;
//
//    private String type;
//
//    @ManyToMany(targetEntity = User.class, fetch = FetchType.LAZY)
//    private Set<User> users;
//
//    @ManyToMany(targetEntity = Role.class, fetch = FetchType.LAZY)
//    private Set<Role> roles;
//
//    @OneToMany(targetEntity = Statement.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    private List<Statement> statements;
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
//    public Set<Role> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(Set<Role> roles) {
//        this.roles = roles;
//    }
//
//    public List<Statement> getStatements() {
//        return statements;
//    }
//
//    public void setStatements(List<Statement> statements) {
//        this.statements = statements;
//    }
//
//    public Policy addRoles(Role...roles){
//        if (getRoles() == null){
//            setRoles(new HashSet<>());
//        }
//        getRoles().addAll(Arrays.asList(roles));
//        return this;
//    }
//
//    public Policy addStatements(Statement...statements){
//        if (getStatements() == null){
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
//        Policy policy = (Policy) o;
//        return getId().equals(policy.getId()) && serviceName.equals(policy.serviceName);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(getId(), serviceName);
//    }
//
//    public Set<User> getUsers() {
//        return users;
//    }
//
//    public void setUsers(Set<User> users) {
//        this.users = users;
//    }
//}
