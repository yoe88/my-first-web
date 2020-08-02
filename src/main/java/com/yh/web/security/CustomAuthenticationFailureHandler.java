package com.yh.web.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//로그인실패시 실행
@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler  {

	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException {
		//로그인 실패 정보를 가지고 있는 객체
		String errMsg = "가입하지 않은 아이디이거나, 잘못된 비밀번호입니다.";
		String id = request.getParameter("id");
		log.info(exception.getMessage());

		//휘발성
		final FlashMap flashMap = new FlashMap();
		flashMap.put("id", id);
		flashMap.put("errMsg", errMsg);
		final FlashMapManager flashMapManager = new SessionFlashMapManager();
		flashMapManager.saveOutputFlashMap(flashMap, request, response);

        response.sendRedirect("login");
	}
}
