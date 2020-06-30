package com.yh.web.service.impl;

import com.yh.web.dao.CommentDao;
import com.yh.web.dto.CommentList;
import com.yh.web.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentDao commentDao;


    public CommentServiceImpl(CommentDao commentDao) {
        log.info("CommentServiceImpl Init...");
        this.commentDao = commentDao;
    }

    @Override
    public List<CommentList> selectCommentByArticleNo(int articleNo) {
        return commentDao.selectCommentByArticleNo(articleNo);
    }

    @Override
    public List<CommentList> selectCommentByCno(int cno) {
        return commentDao.selectCommentByCno(cno);
    }
}
