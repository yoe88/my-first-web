package com.yh.web.dao;

import java.util.List;

import com.yh.web.dto.Member;
import com.yh.web.dto.MemberRole;

public interface MemberDao {
	List<Member> selectAllMemberList();
	void updateMember();
	Member selectMemberById(String loginUserId);
	List<MemberRole> selectRolesById(String id);
	String selectId(String id);
	String selectEmail(String email);
	int insertMember(Member member);
    int insertMemberRole(String id);

}
