package com.yh.web.controller;

import com.yh.web.Utils;
import com.yh.web.dto.board.Board;
import com.yh.web.dto.board.BoardDetail;
import com.yh.web.dto.board.BoardList;
import com.yh.web.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
@RequestMapping(path = "/boards" )
@Controller
public class BoardController {

	private final BoardService boardService;

	private final String notAllow =
				"<script>" +
			"        alert('올바른 접근이 아닙니다.');" +
			"        location.href= ' " + Utils.getRoot() + "/boards';" +
			"    </script>";

	private final String failedWrite =
				"<script>" +
			"        alert('글 작성을 실패했습니다.!');" +
			"        location.href= ' " + Utils.getRoot() + "/boards';" +
			"    </script>";

	public BoardController(BoardService boardService) {
		log.info("BoardController Init...");
		this.boardService = boardService;
	}

	/**
	 * @param field  검색타입
	 * @param query  검색 하고자 하는 내용
	 * @param page_  페이지 숫자
	 * @return  게시글 리스트
	 */
	@PreAuthorize("permitAll()")  //누구나
	@GetMapping(path = {"","/"})
	public ModelAndView boardList(@RequestParam(name = "f",required = false,defaultValue = "title") String field
								 ,@RequestParam(name = "q",required = false,defaultValue = "") String query
								 ,@RequestParam(name = "p",required = false,defaultValue = "1") String page_){
		int page;
		try{  //page 문자열이 숫자로 변환이 안되거나 음수일 경우 1로 초기화
			page = Integer.parseInt(page_);
			if(page < 1) page = 1;
		} catch (NumberFormatException e){
			page = 1;
		}
		//필드명이 유효하지 않은경우 title로 초기화
		Set<String> fieldOption = new HashSet<>(Arrays.asList("title", "writer"));
		if(!fieldOption.contains(field)){
			field = "title";
		}

		Map<String, Object> resultMap = boardService.getBoardList(field, query, page);

		List<BoardList> list = (List<BoardList>) resultMap.get("list"); //게시글 리스트
		int listTotalCount = (int) resultMap.get("count"); 				//검색된 게시글 총개수
		int pageMaxNum =  (int) Math.ceil((listTotalCount/(double)boardService.getListNum())); 	//67개일경우 7
		pageMaxNum = (pageMaxNum ==0) ? 1 : pageMaxNum;

		int listCount = list.size();  //현재 페이지 게시글 개수  ? <= 10
		boolean[] isNow = new boolean[listCount];

		LocalDate now = LocalDate.now(); //현재 일과 게시글 일을 비교한다.
		for (int i = 0; i < listCount; i++) {
			LocalDate date = list.get(i).getRegDate().toLocalDate();
			if(ChronoUnit.DAYS.between(date,now) == 0)
				isNow[i] = true;
		}

		ModelAndView mav = new ModelAndView("/board/boardList");
		mav.addObject("page_title", "자유게시판");
		mav.addObject("f",field);
		mav.addObject("p",page);
		mav.addObject("list",list);
		mav.addObject("listTotalCount",listTotalCount);
		mav.addObject("pageMaxNum",pageMaxNum);
		mav.addObject("isNow",isNow);

		return mav;
	}

	/**
	 * @return 글쓰기 페이지
	 */
	@GetMapping(path = {"/new"})
	public ModelAndView boardForm(){
		ModelAndView mav = new ModelAndView("/board/boardForm");
		mav.addObject("page_title", "글쓰기");
		mav.addObject("title","글 쓰기");
		mav.addObject("action","new");
		return mav;
	}

	/**
	 * @param parent_  참조 부모 글번호
	 * 				답글쓰기 페이지
	 */
	@GetMapping(path = "/{articleNo}/reply")
	public ModelAndView boardForm(@PathVariable("articleNo") String parent_
								, HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		ModelAndView mav = new ModelAndView();
		int parent;
		try{
			parent = Integer.parseInt(parent_);                    //숫자로 변환이 안되거나
			int result = boardService.searchArticleNo(parent);
			if(result == 0) {                                  //참조 부모 글번호가 존재하지 않는경우
				out.write(notAllow);                                //메시지 날리기
			}else{
				mav.setViewName("/board/boardForm");
				mav.addObject("page_title", "답글쓰기");
				mav.addObject("parent",parent);
				mav.addObject("action","new");
			}
			return mav;
		} catch (NumberFormatException e){
			out.write(notAllow);
			return mav;
		}
	}

	/**
	 * @param board     글정보
	 * @param principal 사용자 정보
	 * @param mf        첨부된 파일
	 */
	@PostMapping(path = {"/new"})
	public ModelAndView addBoard(@ModelAttribute Board board
						, Principal principal
						, @RequestParam(value = "file", required = false) MultipartFile mf
						, HttpServletRequest request
						, HttpServletResponse response) throws IOException {
		//설정해야 할것 = 글번호, 작성자, 그룹번호, 아이피
		//자동으로 되는것 = 제목, 내용, 등록날짜, 추천수, 조회수, 부모글번호, 공개여부
		ModelAndView mav = new ModelAndView();

		String ip = request.getRemoteAddr();
		board.setIp(ip);					    //아이피 설정
		board.setWriter(principal.getName());  //작성자 아이디 설정

		int articleNo = boardService.getNextArticleNo();  //다음 글번호 얻기, 시퀀스
		board.setArticleNo(articleNo);  //글번호 설정

		if(board.getParent() != 0){   //답글 쓰기 인 경우,  부모글번호가 존재할때
			int grpNo = boardService.getGrpNo(board.getParent());  //부모 글번호에 대한 그룹번호 얻어오기
			board.setGrpNo(grpNo);  //그룹번호 설정
		}else {
			board.setGrpNo(articleNo);  // 답글쓰기가 아닌경우 글번호와 동일하게 설정
		}

		int result = boardService.addBoard(board,mf);

		if(result != 0) {  //성공적으로 됐을경우 리스트로
			mav.setViewName("redirect: " + Utils.getRoot() + "/boards");
		} else{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();

			out.print(failedWrite);
		}
		return mav;
	}

	/**
	 * @param articleNo 글번호
	 * @return 글번호에 해당하는 상세 페이지
	 */
	@GetMapping(path = "/{articleNo}")
	public ModelAndView boardDetail(@PathVariable("articleNo") int articleNo
									,HttpServletRequest request
									,HttpServletResponse response) throws IOException {
		ModelAndView mav = new ModelAndView();
		mav.addObject("page_title", "상세보기");
		BoardDetail b = boardService.getBoardDetail(articleNo, false);
		if(b == null){
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();

			out.print(notAllow);
		}else{
			mav.addObject("b",b);
			mav.setViewName("/board/boardDetail");
		}
		
		String referer = request.getHeader("REFERER"); //게시판에서 검색한 쿼리스트링 값 넘겨주기
		if(referer != null){
			String qs = null;
			int index = referer.lastIndexOf("?");
			if(index != -1){
				qs = referer.substring(referer.lastIndexOf("?"));
			}
			mav.addObject("qs",qs);
		}

		return mav;
	}


	/**
	 * @param articleNo 글번호
	 * @return 글 수정페이지
	 */
	@GetMapping(path = "/{articleNo}/edit")
	public ModelAndView editBoard(@PathVariable("articleNo") int articleNo
								,HttpServletResponse response) throws IOException {
		ModelAndView mav = new ModelAndView();
		mav.addObject("page_title", "글 수정하기");
		BoardDetail b = boardService.getBoardDetail(articleNo, true);
		if(b == null){
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();

			out.print(notAllow);
		}else{
			mav.addObject("b",b);
			mav.setViewName("/board/boardForm");
			mav.addObject("title","글 수정");
			mav.addObject("action",articleNo+"/edit");
		}
		return mav;
	}

	/**
	 * @param board  제목, 내용
	 * @return 글 수정 실행하기
	 */
	@PostMapping(path = "/{articleNo}/edit")
	public ModelAndView modifyBoard(@ModelAttribute Board board
									//,@PathVariable("articleNo") int articleNo
									,@RequestParam("isDelete") boolean isDelete
									,@RequestParam(value = "file", required = false) MultipartFile mf
									,HttpServletResponse response) throws IOException {
		log.info(board.toString());
		ModelAndView mav = new ModelAndView();
		int result = boardService.modifyBoard(board, mf, isDelete);
		if(result != 0){
			mav.setViewName("redirect: " + Utils.getRoot() + "/boards/" + board.getArticleNo());
		}else{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();

			out.print(failedWrite);
		}
		return  mav;
	}
}
