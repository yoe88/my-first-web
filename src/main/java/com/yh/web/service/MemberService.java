package com.yh.web.service;

import java.util.List;

import com.yh.web.dto.Member;
import com.yh.web.dto.MemberRole;

public interface MemberService{
	//시큐리티 관련
	Member getMemberInfo(String loginUserId);
    List<MemberRole> getMemberRoles(String loginUserId);
	
	List<Member> getAllMemberList();
	int updateMember();
	String searchId(String id);
	String searchEmail(String email);
	int addMember(Member member);

}