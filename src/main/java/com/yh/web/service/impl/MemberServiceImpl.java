package com.yh.web.service.impl;

import com.yh.web.Utils;
import com.yh.web.dao.MemberDao;
import com.yh.web.dto.Member;
import com.yh.web.dto.MemberRole;
import com.yh.web.security.CustomUserDetails;
import com.yh.web.service.MailService;
import com.yh.web.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("memberService")
public class MemberServiceImpl implements MemberService {
    final MemberDao memberDao;
    final PasswordEncoder passwordEncoder;
    final MailService mailService;

    @Autowired
    public MemberServiceImpl(MemberDao memberDao, PasswordEncoder passwordEncoder, MailService mailService) {
        log.info("MemberServiceImpl Init...");
        this.memberDao = memberDao;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    /**
     * @param id 사용자 아이디
     * @return 아이디에 해당하는 사용자 정보
     */
    @Override
    public Member getMemberInfo(String id) {
        Member m = memberDao.selectMemberById(id);
        if(m != null) {
            if (m.getProfileImage() == null) {
                m.setProfileImage("none");
            } else {
                String encodeProfileImageName = Utils.urlEncode(m.getProfileImage());
                m.setProfileImage(encodeProfileImageName);
            }
        }
        return m;
    }

    /**
     * @param id 사용자 아이디
     * @return   아이디에 해당하는 권한
     */
    @Override
    public List<MemberRole> getMemberRoles(String id) {
        return memberDao.selectRolesById(id);
    }

    /**
     * @param id  찾을 아이디
     * @return    일치하는게 있으면 아이디 그대로 리턴 없으면 null
     */
    @Override
    public String findId(String id) {
        return memberDao.selectId(id);
    }

    /**
     * @param email  찾을 이메일
     * @return       일치하는게 있으면 이메일 그대로 리턴 없으면 null
     */
    @Override
    public String findEmail(String email) {
        return memberDao.selectEmail(email);
    }


    @Transactional
    @Override
    public int addMember(Member member) {
        log.info(passwordEncoder.toString());
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        int result = 0;
        result += memberDao.insertMember(member);
        result += memberDao.insertMemberRole(member.getId());
        return  result;
    }

    /** 회원 수정
     * @param member  회원정보
     */
    @Override
    public boolean modifyMember(Member member) {
        int result;
        if(member.getPassword() != null) //수정할 비밀번호가 있으면 암호화 작업
            member.setPassword(passwordEncoder.encode(member.getPassword()));
        
        result = memberDao.updateMember(member);
        if(result != 0){ //업데이트 완료
            CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(member.getProfileImage() != null){ //프로필 이미지 변경 됐으면 세션 수정
                if (member.getProfileImage().equals("")) {
                    user.setProfileImage("none");
                } else {
                    String s = Utils.urlEncode(member.getProfileImage());
                    user.setProfileImage(s);
                }
            }
            if(member.getPassword() != null)  //비밀번호 변경 됐으면 세션 수정
                user.setPassword(member.getPassword());
            return true;
        }else{
            return false;
        }
    }

    /**
     * @param email 이메일
     * @return 이메일과 일치하는 아이디 
     */
    @Override
    public String findIdByEmail(String email) {
        return memberDao.searchIdByEmail(email);
    }

    /**
     * @return   아이디와 이메일이 일치하는 회원이 있으면 1 없으면 0
     */
    @Override
    public int findMemberByIdAndEmail(String id, String email) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("email", email);
        return memberDao.findMemberByIdAndEmail(map);
    }

    /**
     * @param id    아이디
     * @param email 이메일
     * @return  패스워드 변경하고 이메일 전송했으면 true, 실패 false
     */
    @Transactional
    @Override
    public boolean changeTempPassword(String id, String email) {
        //랜덤 코드 생성
        String tempPassword_ = Utils.createRandomCode();
        String tempPassword = passwordEncoder.encode(tempPassword_);
        Member m = new Member();
        m.setId(id);
        m.setPassword(tempPassword);
        int result = memberDao.updateMember(m);
        if(result == 0)
            return false;
        else{
            try{
                String sb = "<html>" +
                            "<body>" +
                            "    <div style=\"border: 2px solid rgb(77, 194, 125); display: inline-block;padding: 2px 5px;\">" +
                            "    <p>변경된 비밀번호입니다.</p>" +
                            "    <p>비밀번호:  <span style=\"font-size: 1.3rem; color: #1d80fb; font-weight: bold; text-decoration: underline;\">" + tempPassword_ + "</span></p>" +
                            "    </div>" +
                            "</body>" +
                            "</html>";
                mailService.sendMail( email, "임시 비밀번호 발급.", sb); //받는사람이메일주소,제목,내용
                return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     *          해당 하는 아이디  사용 불가로 변경 하고 세션 제거
     * @param id         아이디
     */
    @Override
    public int updateEnable(String id, HttpServletRequest request) {
        int result = memberDao.updateEnable(id);
        if(result == 1){
            HttpSession session = request.getSession();
            session.invalidate();
            return 1;
        }

        return 0;
    }
}