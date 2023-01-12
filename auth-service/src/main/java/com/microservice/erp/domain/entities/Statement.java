package com.microservice.erp.domain.entities;

import com.microservice.erp.domain.models.Action;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="statements")
@AttributeOverride(name = "id", column = @Column(name = "statement_id", columnDefinition = "bigint"))
public class Statement extends Auditable<BigInteger, Long> {

    @Enumerated(EnumType.STRING)
    private Action action = Action.None;

    private String resource;

//    @ManyToOne(targetEntity = Policy.class, fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
//    private Policy policy;

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

//    public Policy getPolicy() {
//        return policy;
//    }
//
//    public void setPolicy(Policy policy) {
//        this.policy = policy;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Statement)) return false;
        Statement statement = (Statement) o;
        return getId().equals(statement.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
