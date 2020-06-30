package com.yh.web.dao.impl;

import com.yh.web.dao.MemberDao;
import com.yh.web.dto.Member;
import com.yh.web.dto.MemberRole;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository("memberDao")
public class MemberDaoImpl implements MemberDao {
    private final String MAPPER = "mapper.member.";
    private final SqlSession sqlSession;

    public MemberDaoImpl(SqlSession sqlSession) {
        log.info("MemberDaoImpl Init...");
        this.sqlSession = sqlSession;
    }

    @Override
    public int updateMember(Member member) {
        int result;
        result = sqlSession.update(MAPPER + "updateMember", member);
        return result;
    }

    @Override
    public List<Member> selectAllMemberList() {
        List<Member> membersList;
        membersList = sqlSession.selectList(MAPPER + "selectAllMemberList");
        return membersList;
    }

    @Override
    public Member selectMemberById(String id) {
        return sqlSession.selectOne(MAPPER + "selectMemberById", id);
    }

    @Override
    public List<MemberRole> selectRolesById(String id) {
        return sqlSession.selectList(MAPPER + "selectRolesById", id);
    }

    @Override
    public String selectId(String id) {
        return sqlSession.selectOne(MAPPER + "selectId", id);
    }

    @Override
    public String selectEmail(String email) {
        return sqlSession.selectOne(MAPPER + "selectEmail", email);
    }

    @Override
    public int insertMember(Member member) {
        return sqlSession.insert(MAPPER + "insertMember", member);
    }

    @Override
    public int insertMemberRole(String id) {
        return sqlSession.insert(MAPPER + "insertMemberRole", id);
    }
}
