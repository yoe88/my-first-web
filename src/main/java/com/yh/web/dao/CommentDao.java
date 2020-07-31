package com.yh.web.dao;

import com.yh.web.dto.Comment;
import com.yh.web.dto.CommentList;

import java.util.List;
import java.util.Map;

public interface CommentDao {
    List<CommentList> selectCommentByArticleNo(Map<String, Long> map);

    List<CommentList> selectCommentByCno(Map<String, Long> map);

    int insertComment(Comment comment);

    int updateCommentPub(long cno);

    long selectCommentCountByArticleNo(long articleNo);

    long selectCommentCountByCno(long cno);

    long selectCommentToTalCountByArticleNo(long articleNo);
}
