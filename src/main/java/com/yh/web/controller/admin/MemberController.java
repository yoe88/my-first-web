package com.yh.web.controller.admin;

import com.yh.web.Utils;
import com.yh.web.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Controller("admin.memberController")
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class MemberController {

    private final AdminService adminService;
    public MemberController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * @param field  아이디, 닉네임
     * @param query  검색
     * @param p_     페이지
     * @return       회원리스트
     */
    @GetMapping(path = "/members")
    public ModelAndView members(@RequestParam(name = "f",required = false,defaultValue = "id") String field
            , @RequestParam(name = "q",required = false,defaultValue = "") String query
            , @RequestParam(name = "p",required = false,defaultValue = "1") String p_){
        long page;
        try{  //page 문자열이 숫자로 변환이 안되거나 음수일 경우 1로 초기화
            page = Long.parseLong(p_);
            if(page < 1) page = 1;
        } catch (NumberFormatException e){
            page = 1;
        }
        //필드명이 유효하지 않은경우 id로 초기화
        Set<String> fieldOption = new HashSet<>(Arrays.asList("id", "name"));
        if(!fieldOption.contains(field)){
            field = "id";
        }

        long listTotalCount = adminService.getMembersCount(field,query);				//검색된 회원수
        long pageMaxNum = (long) Math.ceil((listTotalCount/(double)adminService.listNum)); 	//67개일경우 7
        pageMaxNum = (pageMaxNum ==0 ) ? 1 : pageMaxNum;

        if(page > pageMaxNum){											//param p가 페이지 끝 번호보다 큰경우
            ModelAndView mav = new ModelAndView();
            Utils.redirectErrorPage(mav,"존재하지 않는 페이지입니다.\\n확인 후 다시 시도하시기 바랍니다.","/admin/members");
            return mav;
        }

        List<Map<String,Object>> members = adminService.getMembers(field, query, page);    //회원 리스트

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
    public ModelAndView member(@PathVariable("id") String id, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("/admin/member/member");
        Map<String, Object> member = adminService.getMember(id);
        if(member == null){
            Utils.redirectErrorPage(mav,"올바른 접근이 아닙니다.","/admin/members");
        }else {
            mav.addObject("page_title", "회원정보");
            mav.addObject("member",member);
            mav.addObject("qs", Utils.getPreQS(request));
        }
        return mav;
    }

    /**
     * @param id    회원 아이디
     * @param map   enable or role
     * @return     수정 됐으면 1 실패 0
     */
    @PutMapping(path = "/members/{id}", consumes = "application/json")
    public ResponseEntity<Integer> updateMember(@PathVariable("id") String id
                                            , @RequestBody Map<String,Object> map){
        int result = 0;
        String type = (String) map.get("type");   //수정할 타입 enable or role
        if(type.equals("role")){
            String role = (String) map.get("role");
            result = adminService.updateMemberRole(id,role);
        }else if(type.equals("enable")){
            String enable = (String) map.get("enable");
            result = adminService.updateMemberEnable(id,enable);
        }

        return ResponseEntity.ok(result);
    }
}
