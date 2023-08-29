package com.automatedemailing.app.automatedemailing.Controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.automatedemailing.app.automatedemailing.Dto.LoginDto;
import com.automatedemailing.app.automatedemailing.Dto.RegistrationDto;
import com.automatedemailing.app.automatedemailing.Service.UserService;

import jakarta.mail.MessagingException;

@CrossOrigin
@RestController
public class StudentController {

	@Autowired
	private UserService userService;

	@PostMapping(value = "/register")
	public ResponseEntity<String> register(@RequestBody RegistrationDto registerDto) {
		return new ResponseEntity<>(userService.register(registerDto), HttpStatus.OK);
	}

	@PutMapping("/verify-account")
	public ResponseEntity<String> verifyAccount(@RequestParam String email, @RequestParam String otp) {
		return new ResponseEntity<>(userService.verifyAccount(email, otp), HttpStatus.OK);
	}

	@PutMapping("/regenerate-otp")
	public ResponseEntity<String> regenerateOtp(@RequestParam String email) {
		return new ResponseEntity<>(userService.regenerateOtp(email), HttpStatus.OK);
	}

	@PutMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
		return new ResponseEntity<>(userService.login(loginDto), HttpStatus.OK);
	}

	@PutMapping("/forget-password")
	public ResponseEntity<String> forgetPassword(@RequestParam String email) {
		return new ResponseEntity<>(userService.forgetPassword(email), HttpStatus.OK);
	}

	@PutMapping("/set-password")
	public ResponseEntity<String> setPassword(@RequestParam String email, @RequestParam String newPassword) {
		return new ResponseEntity<>(userService.setPassword(email, newPassword), HttpStatus.OK);
	}

	@PostMapping("/send-email")
	public ResponseEntity<String> sendMailWithAttachment(@RequestParam String email, @RequestParam String password,
			@RequestParam MultipartFile attachment) throws MessagingException, IllegalStateException, IOException {
		return new ResponseEntity<>(userService.sendMailWithAttachment(email, password, attachment), HttpStatus.OK);
	}

	@DeleteMapping("/delete-info/{id}")
	public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
		return new ResponseEntity<>(userService.deleteStudent(id), HttpStatus.OK);
	}

}
