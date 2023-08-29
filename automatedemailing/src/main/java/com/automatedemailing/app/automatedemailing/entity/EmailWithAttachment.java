package com.automatedemailing.app.automatedemailing.entity;

import org.springframework.web.multipart.MultipartFile;

public class EmailWithAttachment {

	private MultipartFile attachment;

	public MultipartFile getAttachment() {
		return attachment;
	}

	public void setAttachment(MultipartFile attachment) {
		this.attachment = attachment;
	}
}
