package com.microservice.erp.services.definition;

import com.microservice.erp.domain.beans.models.Otp;

public interface iOtpService {
    boolean verify(Otp otp, String forKey);
    Otp storeOtp(String forKey);
}
