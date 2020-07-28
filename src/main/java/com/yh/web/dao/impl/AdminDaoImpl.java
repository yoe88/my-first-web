package com.yh.web.dao.impl;

import com.yh.web.dao.AdminDao;
import com.yh.web.dto.board.BoardDetail;
import com.yh.web.dto.board.BoardList;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class AdminDaoImpl implements AdminDao {

    private final String MAPPER = "mapper.admin.";
    private final SqlSession sqlSession;

    public AdminDaoImpl(SqlSession sqlSession) {
        log.info("AdminDaoImpl Init...");
        this.sqlSession = sqlSession;
    }

    @Override
    public List<Object> selectMembers(Map<String, Object> map) {
        return sqlSession.selectList(MAPPER + "memberList", map);
    }

    @Override
    public int selectMembersCount(Map<String, Object> map) {
        return sqlSession.selectOne(MAPPER + "memberListCount", map);
    }

    @Override
    public Map<String, Object> selectMemberById(String id) {
        return sqlSession.selectOne( MAPPER + "selectMemberById", id);
    }

    @Override
    public int updateMemberRole(Map<String, String> map) {
        return sqlSession.update( MAPPER + "updateMemberRole", map);
    }

    @Override
    public int updateMemberEnable(Map<String, String> map) {
        return sqlSession.update( MAPPER + "updateMemberEnable", map);
    }

    @Override
    public List<BoardList> selectBoardList(Map<String, Object> map) {
        return sqlSession.selectList(MAPPER + "selectBoardList", map);
    }

    @Override
    public int selectBoardListCount(Map<String, Object> map) {
        return sqlSession.selectOne(MAPPER + "selectBoardListCount", map);
    }

    @Override
    public BoardDetail selectBoardDetailByArticleNo(int articleNo) {
        return sqlSession.selectOne(MAPPER + "selectBoardDetailByArticleNo", articleNo);
    }

    @Override
    public int updateBoardPub(Map<String, Integer> map) {
        return sqlSession.update(MAPPER + "updateBoardPub", map);
    }

    @Override
    public void updateBoardOpenPub(List<String> openNo) {
        sqlSession.update(MAPPER + "updateBoardOpenPub", openNo);
    }

    @Override
    public void updateBoardClosePub(List<String> closeNo) {
        sqlSession.update(MAPPER + "updateBoardClosePub", closeNo);
    }

    @Override
    public List<Map<String, String>> selectGalleryList(Map<String, Integer> map) {
        List<Map<String, String>> maps = sqlSession.selectList(MAPPER + "selectGalleryList", map);
        return maps;
    }

    @Override
    public int selectGalleryListCount() {
        return sqlSession.selectOne(MAPPER + "selectGalleryListCount");
    }

    @Override
    public Map<String, Object> selectGalleryDetail(int gno) {
        return sqlSession.selectOne(MAPPER + "selectGalleryDetail", gno);
    }
}
