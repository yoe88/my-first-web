package com.yh.web.dao;

import java.util.List;
import java.util.Map;

import com.yh.web.dto.Member;
import com.yh.web.dto.MemberRole;

public interface MemberDao {
	List<Member> selectAllMemberList();
	int updateMember(Member member);
	Member selectMemberById(String loginUserId);
	List<MemberRole> selectRolesById(String id);
	String selectId(String id);
	String selectEmail(String email);
	int insertMember(Member member);
    int insertMemberRole(String id);

    String searchIdByEmail(String email);

    int searchMember(Map<String, String> map);

    int updateEnable(String id);
}
