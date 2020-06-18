package com.yh.web.dao.impl;

import com.yh.web.dao.MemberDao;
import com.yh.web.dto.Member;
import com.yh.web.dto.MemberRole;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("memberDao")
public class MemberDaoImpl implements MemberDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String MAPPER = "mapper.member.";
    private final SqlSession sqlSession;

    public MemberDaoImpl(SqlSession sqlSession) {
        logger.info("MemberDaoImpl Init...");
        this.sqlSession = sqlSession;
    }

    //트랜잭션 테스트 메서드
    @Override
    public int updateMember() {

        sqlSession.update(MAPPER + "updatePasswdById1");
        sqlSession.update(MAPPER + "updatePasswdById2");
        return 0;
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
