package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.SignupEmailVerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISignupEmailVerificationCodeRepository extends JpaRepository<SignupEmailVerificationCode, String> {
    SignupEmailVerificationCode findByEmail(String email);
}
