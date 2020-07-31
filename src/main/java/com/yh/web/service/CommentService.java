package com.yh.web.service;

import com.yh.web.dto.Comment;

import java.util.Map;

public interface CommentService {
    //댓글 10개씩 보여주기
    int listNum = 10;

    Map<String, Object> selectCommentByArticleNo(long articleNo, long page);

    Map<String, Object> selectCommentByCno(long cno, long page);

    int addComment(Comment comment);

    int deleteComment(long cno);
}
