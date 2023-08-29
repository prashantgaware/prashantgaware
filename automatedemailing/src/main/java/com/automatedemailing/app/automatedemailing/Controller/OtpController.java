package com.automatedemailing.app.automatedemailing.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.automatedemailing.app.automatedemailing.Util.EmailUtil;
import com.automatedemailing.app.automatedemailing.Util.OtpUtil;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api")
public class OtpController {

	@Autowired
	private OtpUtil otpService;

	@Autowired
	private EmailUtil emailService;

	@PostMapping("/send-otp")
	public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> request) throws MessagingException {
		String email = request.get("email");

		String otp = otpService.generateOtp();

		emailService.sendOtpEmail(email, otp);

		return ResponseEntity.ok("OTP sent successfully");

	}
}
