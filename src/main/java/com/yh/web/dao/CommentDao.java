package com.yh.web.dao;

import com.yh.web.dto.CommentList;

import java.util.List;

public interface CommentDao {
    List<CommentList> selectCommentByArticleNo(int articleNo);

    List<CommentList> selectCommentByCno(int cno);
}
