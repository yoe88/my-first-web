package com.yh.web.dao.impl;

import com.yh.web.dao.BoardDao;
import com.yh.web.dto.BoardList;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class BoardDaoImpl implements BoardDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String MAPPER = "mapper.board.";
    private final SqlSession sqlSession;

    public BoardDaoImpl(SqlSession sqlSession) {
        logger.info("BoardDaoImpl Init...");
        this.sqlSession = sqlSession;
    }

    @Override
    public List<BoardList> selectBoardList(Map map) {
        return sqlSession.selectList(MAPPER + "selectBoardList", map);
    }
}
