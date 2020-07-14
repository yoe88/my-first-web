package com.yh.board.mybatis;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import com.yh.web.dao.AdminDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yh.web.config.RootConfig;
import com.yh.web.dto.Member;
import com.yh.web.service.MemberService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class})
public class MybatisTest {
	@Autowired
	MemberService memberService;

	@Autowired
	AdminDao adminDao;
	
	@Test
	public void selectAllMemberList() {
		List<Member> list = memberService.getAllMemberList();
		assertNotNull(list);
		System.out.println(list);
	}	
	

	
	@Test
	public void getMemberInfor() {
		Member m = memberService.getMemberInfo("nana");
		System.out.println(m);
	}

	@Test
	public void oneToMany(){

	}


}