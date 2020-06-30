package com.yh.web.controller;

import com.yh.web.Utils;
import com.yh.web.dto.Member;
import com.yh.web.security.CustomUserDetails;
import com.yh.web.service.FileService;
import com.yh.web.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.security.Principal;

@Slf4j
@Controller
@RequestMapping(path = "/member")
public class MemberController {
    private final MemberService memberService;
    private final FileService fileService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberController(MemberService memberService
                            ,FileService fileService
                            ,PasswordEncoder passwordEncoder) {
        this.memberService = memberService;
        this.fileService = fileService;
        this.passwordEncoder = passwordEncoder;
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
     * @return OK: 사용가능한 아이디, CONFLICT: 중복된 아이디
     */
    @GetMapping(value = "/checkid", consumes = "text/plain")
    public ResponseEntity isDuplicatedId(@RequestParam(name = "id") String id) {
        log.info("아이디 중복검사: {}", id);
        String result = memberService.searchId(id);
        if(result == null)
            return new ResponseEntity<>("1",HttpStatus.OK);
        else
            return new ResponseEntity<>("0",HttpStatus.CONFLICT);
    }

    /**
     * @param email 이메일
     * @return OK: 사용가능한 이메일, CONFLICT: 중복된 이메일
     */
    @GetMapping(path = "/checkemail", consumes = "text/plain")
    public ResponseEntity isDuplicatedEmail(@RequestParam(name = "email") String email) {
        log.info("이메일 중복검사: {}", email);
        String result = memberService.searchEmail(email);
        if(result == null)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    /**
     * @param code 사용자가 입력한 인증번호
     * @return 1:일치 0:불일치
     */
    @GetMapping(value = "/checkcode")
    public ResponseEntity checkCode(@RequestParam(name = "code", required = false) String code,
                            HttpServletRequest request) {
        log.info("{}",code);
        HttpSession session = request.getSession();
        String code_ = (String) session.getAttribute("code_");
        if (code_ == null) {
            return new ResponseEntity<>("2",HttpStatus.OK); //서버가 재시작하여 세션이 날라간경우
        }
        if (code_.equals(code)) {
            //입력한 인증코드와 실제 인증코드가 일치하면 1전송
            session.removeAttribute("code_");
            return new ResponseEntity<>("1",HttpStatus.OK);
        } else
            return new ResponseEntity<>("0",HttpStatus.OK);
    }

    /**
     * @param password 사용자가 입력한 비밀번호
     * @return 1:일치 0:불일치
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/checkpassword")
    public ResponseEntity<String> checkPassword(@RequestParam(name = "password") String password) {
        log.info("{}",password);
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(passwordEncoder.matches(password,user.getPassword()))
            return new ResponseEntity<>("1",HttpStatus.OK);
        else
            return new ResponseEntity<>("0",HttpStatus.OK);
    }


    /**
     * 회원정보추가
     * @param member 회원정보
     * 성공시 첫페이지로 리턴
     */
    @PostMapping("/new")
    public void addMember(@ModelAttribute Member member
                        ,HttpServletResponse response) throws IOException {
        log.info(member.toString());
        int result;
        result = memberService.addMember(member);
        if(result != 0){
            response.sendRedirect(Utils.getRoot() + "/index");
        } else{
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            String msg = "<script>" +
                    "        alert('회원정보 추가에 실패하였습니다.');" +
                    "        location.href= ' " + Utils.getRoot() + "/index';" +
                    "    </script>";
            out.write(msg);
        }
    }

    /**
     * 내정보
     * @param principal 회원아이디 조회하고
     * @return 회원정보 페이지로 리턴
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ModelAndView myInfo(Principal principal) {
        ModelAndView mav = new ModelAndView("/member/me");
        mav.addObject("page_title", "내정보");
        Member m = memberService.getMemberInfo(principal.getName());
        mav.addObject("m", m);
        return mav;
    }
    
    /**
     * 내정보 수정페이지
     * @param principal 회원아이디 조회하고
     * @return 회원수정 페이지로 리턴
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/edit/{type}")
    public ModelAndView editProfile(Principal principal, @PathVariable("type") String type) {
        log.info("회원정보 수정 type: {}", type);

        ModelAndView mav = new ModelAndView("/member/modify");
        Member m = memberService.getMemberInfo(principal.getName());
        mav.addObject("m", m);

        switch (type) {
            case "profile":
                mav.addObject("page_title", "프로필 수정");
                mav.addObject("type", 1);
                break;
            case "info":
                mav.addObject("page_title", "정보 수정");
                mav.addObject("type", 2);
                break;
            case "password":
                mav.addObject("page_title", "비밀번호 수정");
                mav.addObject("type", 3);
                break;
            case "address":
                mav.addObject("page_title", "주소 수정");
                mav.addObject("type", 4);
                break;
        }
        return mav;
    }

    /**   name, profile 수정
     * @return  프로필 수정이 완료 되면 리턴 1
     */
    //multipartResolver는 put 메서드를 지원하지 않는다.
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = "/edit/profile", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<String> modifyProfile(@ModelAttribute Member member
                                                ,@RequestParam(value = "image", required = false) MultipartFile mf
                                                ,@RequestParam(value = "isDelete", required = false) boolean isDelete
                                                ,HttpServletRequest request){
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        member.setId(user.getUsername());
        String folderName = FileService.profilePath + File.separator + user.getUsername(); //  profile/id

        //---------이미지 관련 수정
        if(mf!=null) { //이미지가 첨부된 경우 업로드하고 멤버객체에 세팅
            log.info("file: {}, size: {}", mf.getOriginalFilename(), mf.getSize());
            String profileImage =  fileService.upload(mf, folderName);
            if(profileImage != null){
                fileService.deleteFile(folderName,user.getProfileImage());
                member.setProfileImage(profileImage);
            }
        }
        if(isDelete) {  //이미지 삭제인 경우
            member.setProfileImage("");
            fileService.deleteFile(folderName,user.getProfileImage());
        }

        //회원정보 수정
        log.info("업데이트 하기전 member: {}",member);
        boolean result = memberService.modifyMember(member);  //db수정
        if(!result){
            return new ResponseEntity<>("Server Error...", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("method: {}",request.getMethod());
        if(request.getMethod().equals("POST")){
            return new ResponseEntity<>("1", HttpStatus.OK);
        }else{
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(Utils.getRoot() + "/member/me"));
            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        }
    }
}
