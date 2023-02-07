package com.microservice.erp.domain.entities;

import javax.persistence.Embeddable;
import java.math.BigInteger;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@Embeddable
public class Username {

    private BigInteger username;

    public Username() {}
    public Username(BigInteger username) {
        this.username = username;
    }

    public BigInteger getUsername() {
        return username;
    }

    public void setUsername(BigInteger username) {
        this.username = username;
    }
}
