package com.yh.web.dao.impl;

import com.yh.web.dao.BoardDao;
import com.yh.web.dto.board.Board;
import com.yh.web.dto.board.BoardDetail;
import com.yh.web.dto.board.BoardFile;
import com.yh.web.dto.board.BoardList;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class BoardDaoImpl implements BoardDao {
    private final String MAPPER = "mapper.board.";
    private final SqlSession sqlSession;

    public BoardDaoImpl(SqlSession sqlSession) {
        log.info("BoardDaoImpl Init...");
        this.sqlSession = sqlSession;
    }

    @Override
    public long selectNextSequence() {
        return sqlSession.selectOne(MAPPER + "selectNextSequence");
    }

    @Override
    public List<BoardList> selectBoardList( Map<String,Object> map) {
        return sqlSession.selectList(MAPPER + "selectBoardList", map);
    }

    @Override
    public long selectBoardListCount( Map<String,Object> map) {
        return sqlSession.selectOne(MAPPER + "selectBoardListCount", map);
    }

    @Override
    public int findBoardByArticleNo(long articleNo) {
        return sqlSession.selectOne(MAPPER + "selectBoardCountByArticleNo", articleNo);
    }

    @Override
    public BoardDetail selectBoardDetailByArticleNo(long articleNo) {
        return sqlSession.selectOne(MAPPER + "selectBoardDetailByArticleNo", articleNo);
    }

    @Override
    public long selectGrpNoByArticleNo(long articleNo) {
        return sqlSession.selectOne(MAPPER + "selectGrpNoByArticleNo", articleNo);
    }

    @Override
    public int insertBoard(Board board) {
        return sqlSession.insert(MAPPER + "insertBoard", board);
    }

    @Override
    public int insertBoardFile(BoardFile boardFile) {
        return sqlSession.insert(MAPPER + "insertBoardFile", boardFile);
    }

    @Override
    public void updateBoardHitByArticleNo(long articleNo) {
        sqlSession.update(MAPPER + "updateBoardHitByArticleNo", articleNo);
    }

    @Override
    public int updateBoard(Board board) {
        return sqlSession.update(MAPPER + "updateBoard", board);
    }

    @Override
    public String selectBoardFileNameByArticleNo(long articleNo) {
        return sqlSession.selectOne(MAPPER + "selectBoardFileNameByArticleNo", articleNo);
    }

    @Override
    public int updateBoardFile(BoardFile boardFile) {
        return sqlSession.update(MAPPER + "updateBoardFile", boardFile);
    }

    @Override
    public int deleteBoardFileByArticleNo(long articleNo) {
        return sqlSession.delete(MAPPER + "deleteBoardFileByArticleNo", articleNo);
    }

    @Override
    public int deleteBoardByArticleNo(long articleNo) {
        return sqlSession.delete(MAPPER + "deleteBoardByArticleNo", articleNo);
    }

    @Override
    public boolean isAlreadyExistsID(Map<String, Object> map) {
        return sqlSession.selectOne(MAPPER + "isAlreadyExistsID", map);
    }

    @Override
    public int insertRecommend(Map<String, Object> map) {
        return sqlSession.insert(MAPPER + "insertRecommend", map);
    }

    @Override
    public List<String> selectTitleLastFive() {
        return sqlSession.selectList(MAPPER + "selectTitleLastFive");
    }

    @Override
    public long selectChildCount(long articleNo) {
        return sqlSession.selectOne(MAPPER + "selectChildCount", articleNo);
    }

    @Override
    public int updateBoardPubByArticleNo(Map<String, Object> map) {
        return sqlSession.update(MAPPER + "updateBoardPubByArticleNo", map);
    }
}
