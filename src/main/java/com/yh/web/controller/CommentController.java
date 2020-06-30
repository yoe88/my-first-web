package com.yh.web.controller;

import com.yh.web.dto.CommentList;
import com.yh.web.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@RequestMapping("/comment")
@Controller
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * @param articleNo 글번호
     * @return  글번호에 해당하는 댓글리스트
     */
    @GetMapping(path = "/{articleNo}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<List<CommentList>> getComment(@PathVariable("articleNo") int articleNo){
        List<CommentList> list = commentService.selectCommentByArticleNo(articleNo);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * @param cno 댓글번호
     * @return  댓글번호에 해당하는 답글 리스트
     */
    @GetMapping(path = "/reply/{cno}", produces = "application/json", consumes = "application/json" )
	public ResponseEntity<List<CommentList>> getReplyComment(@PathVariable("cno") int cno){
		List<CommentList> list = commentService.selectCommentByCno(cno);
        return new ResponseEntity<>(list, HttpStatus.OK);
	}
}
