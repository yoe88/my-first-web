package com.yh.web.service.impl;

import com.yh.web.dao.CommentDao;
import com.yh.web.dto.Comment;
import com.yh.web.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentDao commentDao;

    public CommentServiceImpl(CommentDao commentDao) {
        log.info("CommentServiceImpl Init...");
        this.commentDao = commentDao;
    }

    /**
     * @param articleNo 글번호
     * @param page  페이지번호
     * @return  글번호에 해당하는 (*답글제외) 댓글리스트, 개수, 답글포함 총 총개수
     */
    @Transactional(readOnly = true)
    @Override
    public Map<String, Object> selectCommentByArticleNo(long articleNo, long page) {
        Map<String, Long> map = new HashMap<>();
        long start = 1 + (page-1) * listNum;
        long end = page * listNum;

        map.put("articleNo", articleNo);
        map.put("start",start);
        map.put("end",end);

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("list", commentDao.selectCommentByArticleNo(map));  //댓글리스트 *답글제외
        resultMap.put("count" , commentDao.selectCommentCountByArticleNo(articleNo)); //답글제외한 댓글개수
        resultMap.put("totalCount" , commentDao.selectCommentToTalCountByArticleNo(articleNo)); //답글 포함 총 개수

        return resultMap;
    }

    /**
     * @param cno  댓글번호
     * @param page   페이지 번호
     * @return   댓글번호에 해당하는 답글리스트 ,개수
     */
    @Transactional(readOnly = true)
    @Override
    public Map<String, Object> selectCommentByCno(long cno, long page) {
        Map<String, Long> map = new HashMap<>();
        long start = 1 + (page-1) * listNum;
        long end = page * listNum;

        map.put("cno", cno);
        map.put("start",start);
        map.put("end",end);

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("list", commentDao.selectCommentByCno(map));
        resultMap.put("count" , commentDao.selectCommentCountByCno(cno));

        return resultMap;
    }

    /**  댓글 추가하기
     * @param comment  댓글 정보
     */
    @Override
    public int addComment(Comment comment) {
        return commentDao.insertComment(comment);
    }

    /**  실제 삭제 아닌 비공개 처리
     * @param cno  댓글 번호
     */
    @Override
    public int deleteComment(long cno) {
        return commentDao.updateCommentPub(cno);
    }
}
