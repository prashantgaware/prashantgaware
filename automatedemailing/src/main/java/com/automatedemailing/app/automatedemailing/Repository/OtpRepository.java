package com.automatedemailing.app.automatedemailing.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

@Repository
public class OtpRepository {

	private final Map<String, String> otpMap = new ConcurrentHashMap<>();

	public void storeOtp(String email, String otp) {
		otpMap.put(email, otp);
	}

	public String getOtp(String email) {
		return otpMap.get(email);
	}
}
