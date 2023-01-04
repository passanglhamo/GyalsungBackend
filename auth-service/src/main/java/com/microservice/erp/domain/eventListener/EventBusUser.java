package com.microservice.erp.domain.eventListener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventBusUser {

    BigInteger userId;
    String cid;
    String email;
    String username;
    String password;
    Character isOpenUser;
    String roles;

    public static EventBusUser withId(
            BigInteger userId,
            String cid,
            String email,
            String username,
            String password,
            Character isOpenUser,
            String roles) {
        return new EventBusUser(
                userId,
                cid,
                email,
                username,
                password,
                isOpenUser,
                roles);
    }
}
