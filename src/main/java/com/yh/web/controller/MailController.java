package com.yh.web.controller;

import com.yh.web.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@EnableAsync  //비동기허용
@Controller
public class MailController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private final MailService mailService;

	public MailController(MailService mailService) {
		this.mailService = mailService;
	}

	@RequestMapping(value = "/createcode", method = RequestMethod.GET)
	@ResponseBody
	public String sendSimpleMail(HttpServletRequest request, @RequestParam(name = "email")String receiveEmail) throws Exception {
		request.setCharacterEncoding("utf-8");
		
		try {
			//인증코드 생성
			String randomCode = mailService.createRandomCode();
			logger.info("인증코드생성: {}",randomCode);
			//인증코드 세션 바인딩
			HttpSession session = request.getSession();
			session.setAttribute("code_", randomCode);
			
			StringBuffer sb = new StringBuffer();
			sb.append("<html>");
			sb.append("<body>");
			sb.append("    <div style=\"height: 10px;background-color: rgb(77, 194, 125);\"></div>");
			sb.append("    <p>회원가입 인증번호 입니다.</p>");
			sb.append("    <p>인증번호:  <span style=\"font-size: 1.3rem; color: #1d80fb; font-weight: bold; text-decoration: underline;\">"+ randomCode +"</span></p>");
			sb.append("    <div style=\"height: 10px;background-color: rgb(77, 194, 125);\"></div>");
			sb.append("</body>");
			sb.append("</html>");
			mailService.sendMail( receiveEmail, "회원가입 인증번호입니다.", sb.toString()); //받는사람이메일주소,제목,내용
			//mailService.sendPreConfiguredMessage("안녕~!"); //미리 저장된 송수신주소로 내용만 입력
			return "1";
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}	
	}	
}