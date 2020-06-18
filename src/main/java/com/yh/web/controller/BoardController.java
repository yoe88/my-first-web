package com.yh.web.controller;

import com.yh.web.dto.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/boards" )
@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
public class BoardController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@PreAuthorize("permitAll()") //누구나 접속
	@GetMapping(path = {"","/"})
	public String getBoardList() {
		return "/empty/boards/boardList";
	}
	
	
	@GetMapping(path = "/{num}")
	public String getBoardList(@PathVariable("num") String num) {
		return "/empty/boards/boardDetail";
	}

	@PreAuthorize("permitAll()") //누구나 접속
	@GetMapping(path = "/{articleNo}/reply/{commentNo}", produces = "application/json" )
	@ResponseBody
	public List<Comment> getReplyComment(){
		//Map<String, Object> map = new HashMap<String, Object>();
		logger.info("댓글전송");
		List<Comment> list = new ArrayList<>();
		Comment c = new Comment();
		c.setArticleNo(1);
		c.setCommentNo(1);
		c.setRegDate(LocalDateTime.of(2016, 9, 2, 9, 50, 30));
		c.setWriter("Kang");
		c.setContent("하이염");
		list.add(c);

		return list;
	}
	
}
