package com.yh.web.dao.impl;

import com.yh.web.dao.CommentDao;
import com.yh.web.dto.Comment;
import com.yh.web.dto.CommentList;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class CommentDaoImpl implements CommentDao {

    private final String MAPPER = "mapper.comment.";
    private final SqlSession sqlSession;

    public CommentDaoImpl(SqlSession sqlSession) {
        log.info("CommentDaoImpl Init...");
        this.sqlSession = sqlSession;
    }

    /**
     * @param map 글번호, 시작, 끝 번호가 들어있음
     * @return 댓글 리스트
     */
    @Override
    public List<CommentList> selectCommentByArticleNo(Map<String, Long> map) {
        return sqlSession.selectList(MAPPER + "selectCommentByArticleNo", map);
    }

    /**
     * @param articleNo 글번호
     * @return  글번호에 해당하는 댓글이 몇개 있는지 답글제외
     */
    @Override
    public long selectCommentCountByArticleNo(long articleNo) {
        return sqlSession.selectOne(MAPPER + "selectCommentCountByArticleNo", articleNo);
    }

    /**
     * @param articleNo 글번호
     * @return   댓글 총 개수
     */
    @Override
    public long selectCommentToTalCountByArticleNo(long articleNo) {
        return sqlSession.selectOne(MAPPER + "selectCommentToTalCountByArticleNo", articleNo);
    }

    /**
     * @param map 댓글번호, 시작, 끝 번호가 들어있음
     * @return  댓글에대한 답글 리스트
     */
    @Override
    public List<CommentList> selectCommentByCno(Map<String, Long> map) {
        return sqlSession.selectList(MAPPER + "selectCommentByCno", map);
    }

    @Override
    public long selectCommentCountByCno(long cno) {
        return sqlSession.selectOne(MAPPER + "selectCommentCountByCno", cno);
    }

    @Override
    public int insertComment(Comment comment) {
        return sqlSession.insert(MAPPER + "insertComment", comment);
    }

    @Override
    public int updateCommentPub(long cno) {
        return sqlSession.update(MAPPER + "updateCommentPub", cno);
    }
}
