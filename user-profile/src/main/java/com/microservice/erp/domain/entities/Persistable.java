package com.microservice.erp.domain.entities;

import io.swagger.models.parameters.SerializableParameter;

/**
 * @author Rajib Kumer Ghosh
 *
 */

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public class Persistable<ID,VERSION> implements Serializable {

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
}
