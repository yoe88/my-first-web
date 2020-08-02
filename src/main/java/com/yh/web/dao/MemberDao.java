package com.yh.web.dao;

import com.yh.web.dto.Member;
import com.yh.web.dto.MemberRole;

import java.util.List;
import java.util.Map;

public interface MemberDao {
	int updateMember(Member member);
	Member selectMemberById(String loginUserId);
	List<MemberRole> selectRolesById(String id);
	String selectId(String id);
	String selectEmail(String email);
	int insertMember(Member member);
    int insertMemberRole(String id);

    String searchIdByEmail(String email);

    int findMemberByIdAndEmail(Map<String, String> map);

    int updateEnable(String id);
}
