package com.yh.web.controller;

import com.yh.web.Utils;
import com.yh.web.dto.board.BoardDetail;
import com.yh.web.dto.board.BoardList;
import com.yh.web.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

	private final AdminService adminService;
	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}

	/**
	 * @return 관리자 페이지
	 */
	@GetMapping(path = {"/index",""})
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView("/admin/index");
		mav.addObject("page_title", "관리자");
		return mav;
	}

	/**
	 * @param field  아이디, 닉네임
	 * @param query  검색
	 * @param p_     페이지
	 * @return       회원리스트
	 */
	@GetMapping(path = "/members")
	public ModelAndView members(@RequestParam(name = "f",required = false,defaultValue = "id") String field
								,@RequestParam(name = "q",required = false,defaultValue = "") String query
								,@RequestParam(name = "p",required = false,defaultValue = "1") String p_){
		int page;
		try{  //page 문자열이 숫자로 변환이 안되거나 음수일 경우 1로 초기화
			page = Integer.parseInt(p_);
			if(page < 1) page = 1;
		} catch (NumberFormatException e){
			page = 1;
		}
		//필드명이 유효하지 않은경우 id로 초기화
		Set<String> fieldOption = new HashSet<>(Arrays.asList("id", "name"));
		if(!fieldOption.contains(field)){
			field = "id";
		}

		Map<String, Object> resultMap = adminService.getMembers(field, query, page);
		List<Object> members = (List<Object>) resultMap.get("list");    //게시글 리스트
		int listTotalCount = (int) resultMap.get("count"); 				//검색된 게시글 총개수
		int pageMaxNum =  (int) Math.ceil((listTotalCount/(double)adminService.listNum)); 	//67개일경우 7
		pageMaxNum = (pageMaxNum ==0) ? 1 : pageMaxNum;

		ModelAndView mav = new ModelAndView("/admin/member/members");
		mav.addObject("page_title", "회원리스트");
		mav.addObject("f",field);
		mav.addObject("p",page);
		mav.addObject("list", members);
		mav.addObject("listTotalCount",listTotalCount);
		mav.addObject("pageMaxNum",pageMaxNum);

		return mav;
	}

	/**
	 * @param id         회원 아이디
	 * @return           아이디에 해당하는 회원 정보 페이지
	 */
	@GetMapping(path = "/members/{id}")
	public ModelAndView member(@PathVariable("id") String id, HttpServletRequest request){
		ModelAndView mav = new ModelAndView("/admin/member/member");
		mav.addObject("page_title", "회원정보");
		Map<String, Object> member = adminService.getMember(id);
		mav.addObject("member",member);
		String referer = request.getHeader("REFERER"); //검색한 쿼리스트링 값 넘겨주기
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
	 * @param id    회원 아이디
	 * @param map   enable or role
	 * @return
	 */
	@PutMapping(path = "/members/{id}", consumes = "application/json")
	public ResponseEntity<Integer> updateMember(@PathVariable("id") String id, @RequestBody Map<String,Object> map){
		int result = 0;
		String type = (String) map.get("type");   //수정할 타입 enable or role
		if(type.equals("role")){
			String role = (String) map.get("role");
			result = adminService.updateMemberRole(id,role);
		}else if(type.equals("enable")){
			String enable = (String) map.get("enable");
			result = adminService.updateMemberEnable(id,enable);
		}

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/**
	 * @param field  검색타입
	 * @param query  검색 하고자 하는 내용
	 * @param p_  페이지 숫자
	 * @return  게시글 리스트
	 */
	@GetMapping(path = {"/boards"})
	public ModelAndView boardList(@RequestParam(name = "f",required = false,defaultValue = "title") String field
			,@RequestParam(name = "q",required = false,defaultValue = "") String query
			,@RequestParam(name = "p",required = false,defaultValue = "1") String p_){
		int page;
		try{  //page 문자열이 숫자로 변환이 안되거나 음수일 경우 1로 초기화
			page = Integer.parseInt(p_);
			if(page < 1) page = 1;
		} catch (NumberFormatException e){
			page = 1;
		}
		//필드명이 유효하지 않은경우 title로 초기화
		Set<String> fieldOption = new HashSet<>(Arrays.asList("title", "writer"));
		if(!fieldOption.contains(field)){
			field = "title";
		}
		//게시글 검색값 가져오기
		Map<String, Object> resultMap = adminService.getBoardList(field, query, page);  

		List<BoardList> list = (List<BoardList>) resultMap.get("list"); //게시글 리스트
		int listTotalCount = (int) resultMap.get("count"); 				//검색된 게시글 총개수
		int pageMaxNum =  (int) Math.ceil((listTotalCount/(double)adminService.listNum)); 	//67개일경우 7
		pageMaxNum = (pageMaxNum ==0) ? 1 : pageMaxNum;

		int listCount = list.size();  //현재 페이지 게시글 개수  ? <= 10
		boolean[] isNow = new boolean[listCount];

		LocalDate now = LocalDate.now(); //현재 일과 게시글 일을 비교한다.
		for (int i = 0; i < listCount; i++) {
			LocalDate date = list.get(i).getRegDate().toLocalDate();
			if(ChronoUnit.DAYS.between(date,now) == 0)
				isNow[i] = true;
		}

		ModelAndView mav = new ModelAndView("/admin/board/boardList");
		mav.addObject("page_title", "관리자 게시판");
		mav.addObject("f",field);
		mav.addObject("p",page);
		mav.addObject("list",list);
		mav.addObject("listTotalCount",listTotalCount);
		mav.addObject("pageMaxNum",pageMaxNum);
		mav.addObject("isNow",isNow);

		return mav;
	}

	/**
	 * @param articleNo 글번호
	 * @return 글번호에 해당하는 상세 페이지
	 */
	@GetMapping(path = "/boards/{articleNo}")
	public ModelAndView boardDetail(@PathVariable("articleNo") int articleNo
			, HttpServletRequest request
			, HttpServletResponse response) throws IOException {
		ModelAndView mav = new ModelAndView();
		mav.addObject("page_title", "상세보기");
		BoardDetail b = adminService.getBoardDetail(articleNo);
		if(b == null){
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();

			final String notAllow =
			"	<script>" +
			"        alert('올바른 접근이 아닙니다.');" +
			"        location.href= ' " + Utils.getRoot() + "/admin/boards';" +
			"    </script>";

			out.print(notAllow);
		}else{
			mav.addObject("b",b);
			mav.setViewName("/admin/board/boardDetail");
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

	@PutMapping(path = "/boards/{articleNo}")
	public ResponseEntity<Integer> boardTogglePub(@PathVariable("articleNo") int articleNo, @RequestBody Map<String, Object> map){
		int pub = (int) map.get("pub");
		int result = adminService.updateBoardPub(articleNo, pub);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
