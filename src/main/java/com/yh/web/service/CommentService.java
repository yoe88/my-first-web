package com.yh.web.service;

import com.yh.web.dto.CommentList;

import java.util.List;

public interface CommentService {
    List<CommentList> selectCommentByArticleNo(int articleNo);

    List<CommentList> selectCommentByCno(int cno);
}
