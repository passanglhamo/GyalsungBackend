package com.microservice.erp.domain.entities;



import com.microservice.erp.domain.helper.Action;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name="sa_statements")
public class SaStatement extends Auditable<Long, Long> {

    @Enumerated(EnumType.STRING)
    private Action action = Action.None;

    private String resource;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SaStatement)) return false;
        SaStatement statement = (SaStatement) o;
        return getId().equals(statement.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
