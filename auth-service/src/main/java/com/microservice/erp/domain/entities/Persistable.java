package com.microservice.erp.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.infoworks.lab.rest.models.events.Event;
import com.infoworks.lab.rest.models.events.EventType;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public class Persistable<ID, VERSION> extends Event implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private ID id;

    @Version
    private VERSION version;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public VERSION getVersion() {
        return version;
    }

    public void setVersion(VERSION version) {
        this.version = version;
    }

    @JsonIgnore
    public String getPrimaryKeyName() {
        if (getClass().isAnnotationPresent(AttributeOverride.class)) {
            AttributeOverride attr = getClass().getAnnotation(AttributeOverride.class);
            return attr.column().name();
        }
        return "id";
    }

    @JsonIgnore
    private String uuid;
    @JsonIgnore
    private String timestamp;
    @JsonIgnore
    private EventType eventType;

}
