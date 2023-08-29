package com.automatedemailing.app.automatedemailing.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.automatedemailing.app.automatedemailing.Dto.LoginDto;
import com.automatedemailing.app.automatedemailing.Dto.RegistrationDto;
import com.automatedemailing.app.automatedemailing.Repository.StudentRepository;
import com.automatedemailing.app.automatedemailing.Util.EmailUtil;
import com.automatedemailing.app.automatedemailing.Util.OtpUtil;
import com.automatedemailing.app.automatedemailing.entity.Student;

import jakarta.mail.MessagingException;

@Service
public class UserService {

	@Autowired
	private OtpUtil otpUtil;
	@Autowired
	private EmailUtil emailUtil;
	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public String register(RegistrationDto registerDto) {

		Optional<Student> fullNameEntry = studentRepository.findByFullName(registerDto.getFullName());
		Optional<Student> emailEntry = studentRepository.findByEmail(registerDto.getEmail());
		if (fullNameEntry.isPresent()) {
			// throw new ResponseStatusException(HttpStatus.OK, "Username already exists!");
			return "User Already Exist";
		}
		if (emailEntry.isPresent()) {
			// throw new ResponseStatusException(HttpStatus.OK, "Email already exists!");
			return "User Email Already Exist";
		}
		if (validateInputs(registerDto)) { // filter as you need in this method
			studentRepository.save(registerDto);
			System.out.println("New user submission!");
		}

		String otp = otpUtil.generateOtp();
		try {
			emailUtil.sendOtpEmail(registerDto.getEmail(), otp);
		} catch (MessagingException e) {
			throw new RuntimeException("Unable to send otp please try again");
		}
		Student stud = new Student();
		stud.setFullName(registerDto.getFullName());
		stud.setEmail(registerDto.getEmail());
		stud.setPassword(passwordEncoder.encode(registerDto.getPassword()));
		stud.setOtp(otp);
		stud.setOtpTime(LocalDateTime.now());
		studentRepository.save(stud);
		return "User registration successful";
	}

	private boolean validateInputs(RegistrationDto registerDto) {

		return false;
	}

	public String verifyAccount(String email, String otp) {
		Student stud = studentRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
		if (stud.getOtp().equals(otp)
				&& Duration.between(stud.getOtpTime(), LocalDateTime.now()).getSeconds() < (3 * 60)) {
			stud.setActive(true);
			studentRepository.save(stud);
			return "OTP verified you can login";
		}
		return "Please regenerate otp and try again";
	}

	public String regenerateOtp(String email) {
		Student stud = studentRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
		String otp = otpUtil.generateOtp();
		try {
			emailUtil.sendOtpEmail(email, otp);
		} catch (MessagingException e) {
			throw new RuntimeException("Unable to send otp please try again");
		}
		stud.setOtp(otp);
		stud.setOtpTime(LocalDateTime.now());
		studentRepository.save(stud);
		return "Email sent... please verify account within 3 minute";
	}

	public String login(LoginDto loginDto) {
		Student stud = studentRepository.findByEmail(loginDto.getEmail())
				.orElseThrow(() -> new RuntimeException("User not found with this email: " + loginDto.getEmail()));
		if (!passwordEncoder.matches(loginDto.getPassword(), stud.getPassword())) {
			return "Password is incorrect";
		} else if (!stud.isActive()) {
			return "your account is not verified";
		}

		return "Login successful";
	}

	@SuppressWarnings("unused")
	public String forgetPassword(String email) {

		Student stud = studentRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
		try {
			emailUtil.sendSetPasswordEmail(email);
		} catch (MessagingException e) {
			throw new RuntimeException("Unable to send Reset Password Link please try again");
		}
		return "Email Sent Successfully";
	}

	public String setPassword(String email, String newPassword) {
		Student stud = studentRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
		stud.setPassword(passwordEncoder.encode(newPassword));
		studentRepository.save(stud);
		return "Password Reset Successfully";
	}

	public String sendMailWithAttachment(String email, String password, MultipartFile attachment)
			throws MessagingException, IllegalStateException, IOException {
		@SuppressWarnings("unused")
		Student stud = studentRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
		emailUtil.sendAttachment(email, password, attachment);
		return "Email with Attachment Sent Successfully";

	}

	public String deleteStudent(Long id) {
		@SuppressWarnings("unused")
		Student stud = studentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found with this id: " + id));
		studentRepository.deleteById(id);
		return "Information deleted";
	}

}
