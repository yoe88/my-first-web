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
    public int selectNextSequence() {
        return sqlSession.selectOne(MAPPER + "selectNextSequence");
    }

    @Override
    public List<BoardList> selectBoardList( Map<String,Object> map) {
        return sqlSession.selectList(MAPPER + "selectBoardList", map);
    }

    @Override
    public int selectBoardListCount( Map<String,Object> map) {
        return sqlSession.selectOne(MAPPER + "selectBoardListCount", map);
    }

    @Override
    public int selectBoardCountByArticleNo(int articleNo) {
        return sqlSession.selectOne(MAPPER + "selectBoardCountByArticleNo", articleNo);
    }

    @Override
    public BoardDetail selectBoardDetailByArticleNo(int articleNo) {
        return sqlSession.selectOne(MAPPER + "selectBoardDetailByArticleNo", articleNo);
    }

    @Override
    public int selectGrpNoByArticleNo(int articleNo) {
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
    public void updateBoardHitByArticleNo(int articleNo) {
        sqlSession.update(MAPPER + "updateBoardHitByArticleNo", articleNo);
    }

    @Override
    public int updateBoard(Board board) {
        return sqlSession.update(MAPPER + "updateBoard", board);
    }

    @Override
    public String selectBoardFileNameByArticleNo(int articleNo) {
        return sqlSession.selectOne(MAPPER + "selectBoardFileNameByArticleNo", articleNo);
    }

    @Override
    public int updateBoardFile(BoardFile boardFile) {
        return sqlSession.update(MAPPER + "updateBoardFile", boardFile);
    }

    @Override
    public int deleteBoardFileByArticleNo(int articleNo) {
        return sqlSession.delete(MAPPER + "deleteBoardFileByArticleNo", articleNo);
    }

    @Override
    public int deleteBoardByArticleNo(int articleNo) {
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
}
