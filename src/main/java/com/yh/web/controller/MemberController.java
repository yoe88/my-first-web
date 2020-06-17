package com.yh.web.controller;

import com.yh.web.dto.Member;
import com.yh.web.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
@RequestMapping(path = "/member")
public class MemberController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * @return 로그인페이지
     */
    @RequestMapping(path = "/login")
    public String login() {
        return "/empty/member/login";
    }

    /**
     * @return 회원가입페이지
     */
    @GetMapping(path = "/register")
    public String register() {
        return "/empty/member/register";
    }

    /**
     * @param id 아이디
     * @return true: 중복된 아이디, false: 존재하지 않는 아이디
     */
    @GetMapping("/checkid")
    @ResponseBody
    public boolean isDuplicatedId(@RequestParam(name = "id") String id) {
        logger.info("아이디 중복검사: {}", id);
        String result = memberService.searchId(id);
        return result != null;
    }

    /**
     * @param email 이메일
     * @return true: 중복된 이메일, false: 존재하지 않는 이메일
     */
    @GetMapping("/checkemail")
    @ResponseBody
    public boolean isDuplicatedEmail(@RequestParam(name = "email") String email) {
        logger.info("이메일 중복검사: {}", email);
        String result = memberService.searchEmail(email);
        return result != null;
    }

    /**
     * @param code 사용자가 입력한 인증번호
     * @return 1:일치 0:불일치
     */
    @GetMapping("/checkcode")
    @ResponseBody
    public String checkCode(@RequestParam(name = "code", required = false) String code,
                            HttpServletRequest request) {
        System.out.println(code);
        HttpSession session = request.getSession();
        String code_ = (String) session.getAttribute("code_");
        if (code_ == null) {
            return "2"; //서버가 재시작하여 세션이 날라간경우
        }
        if (code_.equals(code)) {
            //입력한 인증코드와 실제 인증코드가 일치하면 1전송
            session.removeAttribute("code_");
            return "1";
        } else
            return "0";
    }

    /**
     * @param member 회원정보
     * @return 첫페이지로 리턴
     */
    @PostMapping("/new")
    public String addMember(@ModelAttribute Member member) {
        memberService.addMember(member);
        return "redirect:/index";
    }

    /**
     * @param principal 회원아이디 조회하고
     * @return 회원정보 페이지로 리턴
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public ModelAndView profile(Principal principal) {
        ModelAndView mav = new ModelAndView("/member/profile");
        mav.addObject("page_title", "내정보");
        Member m = memberService.getMemberInfor(principal.getName());
        mav.addObject("m", m);
        return mav;
    }
}
