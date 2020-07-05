package com.yh.web.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Service("mailService")
public class MailService {
	private final JavaMailSender mailSender;

	public MailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * @param to 받는이 이메일주소
	 * @param subject  제목
	 * @param body   내용
	 */
	@Async
	public void sendMail(String to, String subject, String body) {
		MimeMessage message = mailSender.createMimeMessage();
		try {
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8"); //메일설정위힌 객체
			messageHelper.setFrom("myEmail","YH"); 				//발신자 별칭 설정
			messageHelper.setTo(to); 										//수신자
			//messageHelper.setCc("abc@daum.net"); 							//참조자 설정
			messageHelper.setSubject(subject); 								//제목
			messageHelper.setText(body,true);  						//내용
			mailSender.send(message); 										//전송
		} catch (MessagingException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	

	//출처 https://ktko.tistory.com/entry

	/**
	 * @return 랜덤코드 6자리
	 */
	public static String createRandomCode() {
		int certCharLength = 6;
		final char[] characterTable = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
				'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };

		Random random = new Random(System.currentTimeMillis());
		int tablelength = characterTable.length;
		StringBuilder buf = new StringBuilder();

		for (int i = 0; i < certCharLength; i++) {
			buf.append(characterTable[random.nextInt(tablelength)]);
		}
		return buf.toString();
	}
}
