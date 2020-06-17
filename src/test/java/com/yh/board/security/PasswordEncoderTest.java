package com.yh.board.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yh.web.config.RootConfig;
import com.yh.web.config.SecurityConfig;

//﻿스프링 빈 컨테이너가 관리하는 빈을 테스트하려면 @RunWith이 필요하다.
@RunWith(SpringJUnit4ClassRunner.class)
//설정이 들어있는 config를 알려줘야한다.
@ContextConfiguration(classes = {RootConfig.class, SecurityConfig.class})
public class PasswordEncoderTest {
  @Autowired
  PasswordEncoder passwordEncoder;

  @Test
  public void passwordEncode() throws Exception{
  	//passwordEncoder가 가지고 있는 메서드   암호화시켜준다.
      System.out.println(passwordEncoder.encode("0806"));
  }
  
  @Test
  public void passwordTest() throws Exception{
  	//암호되는 값은 매번 다른데 matches를 하면 원래값이랑 같은지 반환한다.
  	String encodePasswd = "$2a$10$u5lK15OuNAfjjrtGN/g1ge6Dn318MmK808EdWp1iGGwZegIRBOZIS";
  	String password = "1234";
  	boolean test = passwordEncoder.matches(password, encodePasswd);
  	System.out.println(test);
  }
}