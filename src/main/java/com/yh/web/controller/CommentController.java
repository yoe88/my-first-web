package com.yh.web.controller;

import com.yh.web.dto.Comment;
import com.yh.web.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

@Slf4j
@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
@RequestMapping("/comment")
@Controller
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * @param articleNo 글번호
     * @param p_        댓글 페이지
     * @return  글번호에 해당하는 댓글리스트
     */
    @GetMapping(path = "/{articleNo}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Map<String,Object>> getComment(@PathVariable("articleNo") int articleNo
            ,@RequestParam(value = "p", required = false, defaultValue = "1") String p_){

        int page;
        try{  //p_ 문자열이 숫자로 변환이 안되거나 음수일 경우 1로 초기화
            page = Integer.parseInt(p_);
            if(page < 1) page = 1;
        } catch (NumberFormatException e){
            page = 1;
        }
        Map<String,Object> resultMap = commentService.selectCommentByArticleNo(articleNo, page);

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    /**
     * @param cno 댓글번호
     * @return  댓글번호에 해당하는 답글 리스트
     */
    @GetMapping(path = "/reply/{cno}", produces = "application/json", consumes = "application/json" )
	public ResponseEntity<Map<String,Object>> getReplyComment(@PathVariable("cno") int cno
            ,@RequestParam(value = "p", required = false, defaultValue = "1") String p_){

        int page;
        try{  //p_ 문자열이 숫자로 변환이 안되거나 음수일 경우 1로 초기화
            page = Integer.parseInt(p_);
            if(page < 1) page = 1;
        } catch (NumberFormatException e){
            page = 1;
        }

        Map<String,Object> resultMap = commentService.selectCommentByCno(cno, page);
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}

    /**
     * @param comment   댓글 정보
     * @return 댓글 추가가 되었으면 1 실패했으면 0
     */
    @PostMapping(path = "", consumes = "application/json")
    public ResponseEntity<Integer> addComment(@RequestBody Comment comment
                        , HttpServletRequest request, Principal principal){

        String ip = request.getRemoteAddr();
        String id = principal.getName();

        comment.setWriter(id);
        comment.setIp(ip);

        int result = commentService.addComment(comment);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * @param cno 댓글번호
     * @return  댓글삭제 성공 1, 실패 0
     */
    @DeleteMapping(path = "/{cno}",  consumes = "text/plain")
    public ResponseEntity<Integer> deleteComment(@PathVariable("cno") int cno){
        int result = commentService.deleteComment(cno);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
