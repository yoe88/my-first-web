package com.yh.web.dao.impl;

import com.yh.web.dao.CommentDao;
import com.yh.web.dto.CommentList;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class CommentDaoImpl implements CommentDao {

    private final String MAPPER = "mapper.comment.";
    private final SqlSession sqlSession;

    public CommentDaoImpl(SqlSession sqlSession) {
        log.info("CommentDaoImpl Init...");
        this.sqlSession = sqlSession;
    }

    @Override
    public List<CommentList> selectCommentByArticleNo(int articleNo) {
        return sqlSession.selectList(MAPPER + "selectCommentByArticleNo", articleNo);
    }

    @Override
    public List<CommentList> selectCommentByCno(int cno) {
        return sqlSession.selectList(MAPPER + "selectCommentByCno", cno);
    }
}
