package com.microservice.erp.domain.eventListener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import scala.Char;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventBusUser {

    BigInteger userId;
    Character status;
    String cid;
    String dob;
    String email;
    String mobileNo;
    String username;
    String password;
    Character userType;
    List<BigInteger> roles;

    public static EventBusUser withId(
            BigInteger userId,
            Character status,
            String cid,
            String dob,
            String email,
            String mobileNo,
            String username,
            String password,
            Character userType,
            List<BigInteger> roles) {
        return new EventBusUser(
                userId,
                status,
                cid,
                dob,
                email,
                mobileNo,
                username,
                password,
                userType,
                roles);
    }
}
