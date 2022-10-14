package com.microservice.erp.domain.entities;

import com.microservice.erp.domain.helper.ERole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "sa_roles")
public class Role {
    @Id
    private Integer roleId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    public Role() {
    }
}