package com.microservice.erp.domain.helper;

import com.microservice.erp.domain.entities.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CustomGrantedAuthority implements GrantedAuthority {

    private final SimpleGrantedAuthority simpleGrantedAuthority;
    private final Character isOpenUser;

    public CustomGrantedAuthority(String roleName, Character isOpenUser) {
        this.simpleGrantedAuthority = new SimpleGrantedAuthority(roleName);
        this.isOpenUser = isOpenUser;
    }

    @Override
    public String getAuthority() {
        return simpleGrantedAuthority.getAuthority();
    }

    public Character getIsOpenUser() {
        return isOpenUser;
    }

    public static Collection<? extends GrantedAuthority> getAuthorities(List<Role> roles) {
        if (roles == null) {
            return Collections.emptyList();
        }
        return roles.stream()
                .map(role -> new CustomGrantedAuthority(role.getRoleName(), role.getUserType()))
                .collect(Collectors.toList());
    }
}