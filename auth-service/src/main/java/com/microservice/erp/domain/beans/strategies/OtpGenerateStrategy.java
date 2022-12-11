package com.microservice.erp.domain.beans.strategies;

public interface OtpGenerateStrategy<OTP> {
    OTP generateOtp(int length, long duration);
}
