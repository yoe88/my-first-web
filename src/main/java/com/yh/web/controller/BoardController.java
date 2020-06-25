package com.yh.web.controller;

import com.yh.web.dto.Board;
import com.yh.web.dto.BoardList;
import com.yh.web.dto.Comment;
import com.yh.web.service.BoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Controller
@RequestMapping(path = "/boards" )
@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
public class BoardController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final BoardService boardService;

	@Autowired
	public BoardController(BoardService boardService) {
		logger.info("BoardController Init...");
		this.boardService = boardService;
	}


	/**
	 * @return 게시판 글 리스트
	 */
	@PreAuthorize("permitAll()")
	@GetMapping(path = {"","/"})
	public ModelAndView boardList(@RequestParam(name = "f",required = false,defaultValue = "title") String field,
								  @RequestParam(name = "q",required = false,defaultValue = "") String query,
								  @RequestParam(name = "p",required = false,defaultValue = "1") String page_) {

		Set<String> fieldOption = new HashSet<>(Arrays.asList("","title", "writer"));
		int page;
		try{
			page = Integer.parseInt(page_);
		} catch (NumberFormatException e){
			page = 1;
		}
		if(!fieldOption.contains(field)){
			field = "title";
		}

		logger.info("{}, {}, {}", field, query, page);
		List<BoardList> list = boardService.getBoardList(field, query, page);


		/*int listCnt = list.size();  //현재 페이지 게시글 개수  ? < 10
		boolean[] isNow = new boolean[listCnt];
		int listTotalCnt = boardService.getListCount(field, query); //검색된 게시글 총개수
		int pageMaxNum =  (int) Math.ceil((listTotalCnt/10.0)); //67개일경우 7

		LocalDateTime now = LocalDateTime.now(); //현재 일과 DB 일을 비교한다.
		for (int i = 0; i < listCnt; i++) {
			LocalDateTime dateTime = list.get(i).getRegDate();

			//24시간을 지나지 않으면서     &&     일이 같으면
			isNow[i] = ChronoUnit.SECONDS.between(dateTime, now) < 86399 && (now.getDayOfMonth() == dateTime.getDayOfMonth() );

			//일이같으면        년도나 월이 다를경우에도 적용이 되므로 위 방법을 사용
			//isNow[i] = (now.getDayOfMonth() == dateTime.getDayOfMonth() );
		}*/


		ModelAndView mav = new ModelAndView("/board/boardList");
		mav.addObject("page_title", "자유게시판");
		mav.addObject("list",list);
		return mav;
	}

	/**
	 * @return 글쓰기 페이지
	 */
	@PreAuthorize("permitAll()")
	@GetMapping(path = {"/new"})
	public ModelAndView boardForm() {
		ModelAndView mav = new ModelAndView("/board/boardForm");
		mav.addObject("page_title", "글쓰기");
		return mav;
	}

	/**
	 * @param num 글번호
	 * @return 글번호에 해당하는 페이지
	 */
	@PreAuthorize("permitAll()") //누구나 접속
	@GetMapping(path = "/{num}")
	public ModelAndView boardDetail(@PathVariable("num") String num) {
		ModelAndView mav = new ModelAndView("/boards/boardDetail");
		mav.addObject("page_title", "상세보기");
		return mav;
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
