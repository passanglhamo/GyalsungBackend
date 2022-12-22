package com.microservice.erp.services.iServices;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;

public interface ISaUserSaRoleMapService {
    ResponseEntity<?> addSaUserSaRole(@Valid AddSaUserSaRoleCommand command);

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class AddSaUserSaRoleCommand {

        private BigInteger userId;

        private List<BigInteger> roleIds;
    }

}
