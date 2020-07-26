package com.yh.web.service;

import java.util.List;

import com.yh.web.dto.Member;
import com.yh.web.dto.MemberRole;

import javax.servlet.http.HttpServletRequest;

public interface MemberService{
	//시큐리티 관련
	Member getMemberInfo(String loginUserId);
    List<MemberRole> getMemberRoles(String loginUserId);
	
	List<Member> getAllMemberList();
	String findId(String id);
	String findEmail(String email);
	int addMember(Member member);
    boolean modifyMember(Member member);

    String findIdByEmail(String email);

    int searchMember(String id, String email);

    boolean changeTempPassword(String id, String email);

    int updateEnable(String id, HttpServletRequest request);
}