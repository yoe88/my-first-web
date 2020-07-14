package com.yh.web.controller;

import com.yh.web.Utils;
import com.yh.web.dto.Member;
import com.yh.web.security.CustomUserDetails;
import com.yh.web.service.FileService;
import com.yh.web.service.MailService;
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
    private final MailService mailService;

    @Autowired
    public MemberController(MemberService memberService
                            ,FileService fileService
                            ,PasswordEncoder passwordEncoder
                            ,MailService mailService) {
        this.memberService = memberService;
        this.fileService = fileService;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
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
    public ResponseEntity<String> isDuplicatedId(@RequestParam(name = "id") String id) {
        log.info("아이디 중복검사: {}", id);
        String result = memberService.findId(id);
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
    public ResponseEntity<String> isDuplicatedEmail(@RequestParam(name = "email") String email) {
        log.info("이메일 중복검사: {}", email);
        String result = memberService.findEmail(email);
        if(result == null)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @RequestMapping(value = "/createcode", method = RequestMethod.GET, consumes = "text/plain")
    @ResponseBody
    public String sendSimpleMail(HttpServletRequest request, @RequestParam(name = "email")String receiveEmail) throws Exception {
        request.setCharacterEncoding("utf-8");

        try {
            //인증코드 생성
            String randomCode = Utils.createRandomCode();
            log.info("인증코드생성: {}",randomCode);
            //인증코드 세션 바인딩
            HttpSession session = request.getSession();
            session.setAttribute("code_", randomCode);

            String sb = "<html>" +
                    "<body>" +
                    "    <div style=\"border: 2px solid rgb(77, 194, 125); display: inline-block;padding: 2px 5px;\">" +
                    "    <p>회원가입 인증번호 입니다.</p>" +
                    "    <p>인증번호:  <span style=\"font-size: 1.3rem; color: #1d80fb; font-weight: bold; text-decoration: underline;\">" + randomCode + "</span></p>" +
                    "    </div>" +
                    "</body>" +
                    "</html>";
            mailService.sendMail( receiveEmail, "회원가입 인증번호입니다.", sb); //받는사람이메일주소,제목,내용
            //mailService.sendPreConfiguredMessage("안녕~!"); //미리 저장된 송수신주소로 내용만 입력
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    /** 회원 가입 페이지에서 사용
     * @param code 사용자가 입력한 인증번호
     * @return 1:일치 0:불일치
     */
    @GetMapping(value = "/checkcode")
    public ResponseEntity<String> checkCode(@RequestParam(name = "code", required = false) String code,
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

    /** 비밀번호 수정 페이지에서 사용
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

    /**
     *  로그인 페이지에서 아이디 찾기 기능
     * @param email 이메일
     * @return  이메일에 일치하는 아이디, 없으면 null
     */
    @GetMapping(path = "/findId", consumes = "text/plain")
    public ResponseEntity<String> find(@RequestParam(name = "email") String email){
        String ID = memberService.findIdByEmail(email);
        return new ResponseEntity<>(ID ,HttpStatus.OK);
    }

    /**
     *  로그인 페이지에서 비밀번호 찾기 기능
     * @param id    아이디
     * @param email 이메일
     * @return  아이디와 이메일이 일치하는 회원수 1, 불일치 0
     */
    @GetMapping(path = "/findMember", consumes = "text/plain")
    public ResponseEntity<Integer> find(@RequestParam(name = "id") String id ,@RequestParam(name = "email") String email){
        int result = memberService.searchMember(id, email);
        return new ResponseEntity<>(result ,HttpStatus.OK);
    }

    /**
     *  로그인 페이지에서 비밀빈호 찾기 후 새로운 비밀번호 발급
     * @param id    아이디    임시 비밀번호로 대체할 아이디
     * @param email 이메일    임시 비밀번호를 받을 이메일
     */
    @GetMapping(path = "/newPassword", consumes = "text/plain")
    public ResponseEntity<String> newPassword(@RequestParam(name = "id") String id ,@RequestParam(name = "email") String email){
        boolean result = memberService.changeTempPassword(id,email);
        if(result)
            return new ResponseEntity<>("OK",HttpStatus.OK);
        else
            return new ResponseEntity<>("FAIL",HttpStatus.OK);
    }

}
