package com.automatedemailing.app.automatedemailing.Util;

import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;

@Component
public class EmailUtil {

	@Autowired
	private JavaMailSender javaMailSender;

	public void sendOtpEmail(String email, String otp) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
		mimeMessageHelper.setTo(email);
		mimeMessageHelper.setSubject("Verify Your Account");
		mimeMessageHelper.setText("""
				Dear Student Your One Time Password is  : %s
				 """.formatted(otp), true);
		javaMailSender.send(mimeMessage);

	}

	public String sendAttachment(String email, String password, MultipartFile attachment)
			throws IllegalStateException, MessagingException, IOException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
		mimeMessageHelper.setTo(email);
		mimeMessageHelper.setSubject("Your Login Credentials ");
		mimeMessageHelper.setText("""
				Dear Student Your Login Credentials are : <br>
				Username  : %s<br>
				Password  : %s<br>
				""".formatted(email, password), true);

		String filename = attachment.getOriginalFilename();
		File f = convertMultipartToFile(attachment, filename);
		FileSystemResource file = new FileSystemResource(f);
		mimeMessageHelper.addAttachment(filename, file);
		javaMailSender.send(mimeMessage);
		return "Email Sent Successfully";
	}

	private static File convertMultipartToFile(MultipartFile multipartFile, String filename)
			throws IllegalStateException, IOException {
		File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + filename);
		multipartFile.transferTo(convFile);
		return convFile;
	}

	public void sendSetPasswordEmail(String email) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
		mimeMessageHelper.setTo(email);
		mimeMessageHelper.setSubject("Reset Password");
		mimeMessageHelper.setText("""
				Dear Student Click Below Link To Reset Your Password :
				""".formatted(email), true);
		mimeMessageHelper.setText(
				"""

						<div>
						  <a href="http://localhost:8080/Reset-password?email=%s"target="_blank">Reset</a> click link to Reset Password
						</div>

						"""
						.formatted(email),true);

		javaMailSender.send(mimeMessage);

	}

}
