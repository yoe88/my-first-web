package com.yh.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/boards" )
@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
public class BoardController {
	
	@PreAuthorize("isAnonymous()") //누구나 접속
	@GetMapping(path = {"","/"})
	public String getBoardList() {
		return "/boards/boardList";
	}
	
	
	@GetMapping(path = "/{num}")
	public String getBoardList(@PathVariable("num") String num) {
		return "/boards/boardDetail";
	}
	
}
