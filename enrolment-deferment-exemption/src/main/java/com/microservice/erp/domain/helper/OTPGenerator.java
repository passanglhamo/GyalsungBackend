package com.microservice.erp.domain.helper;

import java.util.Random;

public class OTPGenerator {
	public static String generateOtp() {
		Integer min = 100000;  // Minimum 6-digit number
		Integer max = 999999;  // Maximum 6-digit number

		Random random = new Random();
		Integer randomNumber = random.nextInt(max - min + 1) + min;

		return randomNumber.toString();
	}

}