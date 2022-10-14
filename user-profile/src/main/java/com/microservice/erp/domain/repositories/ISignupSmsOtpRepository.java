package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.SignupSmsOtp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISignupSmsOtpRepository extends JpaRepository<SignupSmsOtp, String> {
    SignupSmsOtp findByMobileNo(String mobileNo);
}
