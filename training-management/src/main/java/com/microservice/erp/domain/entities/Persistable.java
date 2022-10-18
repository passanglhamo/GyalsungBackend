package com.microservice.erp.domain.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Rajib Kumer Ghosh
 */

@MappedSuperclass
public class Persistable<ID, VERSION> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
