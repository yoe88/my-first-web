package com.yh.web.dao;

import com.yh.web.dto.Comment;
import com.yh.web.dto.CommentList;

import java.util.List;
import java.util.Map;

public interface CommentDao {
    List<CommentList> selectCommentByArticleNo(Map<String, Integer> map);

    List<CommentList> selectCommentByCno(Map<String, Integer> map);

    int insertComment(Comment comment);

    int updateComment(int cno);

    int selectCommentCountByArticleNo(int articleNo);

    int selectCommentCountByCno(int cno);

    int selectCommentToTalCountByArticleNo(int articleNo);
}
