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

    @Transactional(readOnly = true)
    @Override
    public Member getMemberInfo(String id) {
        return memberDao.selectMemberById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MemberRole> getMemberRoles(String id) {
        return memberDao.selectRolesById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Member> getAllMemberList() {
        return memberDao.selectAllMemberList();
    }

    @Transactional(readOnly = true)
    @Override
    public String findId(String id) {
        return memberDao.selectId(id);
    }

    @Transactional(readOnly = true)
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

    @Transactional
    @Override
    public boolean modifyMember(Member member) {
        int result;
        if(member.getPassword() != null) //수정할 비밀번호가 있으면 암호화 작업
            member.setPassword(passwordEncoder.encode(member.getPassword()));
        
        result = memberDao.updateMember(member); //
        if(result != 0){ //업데이트 완료
            CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(member.getProfileImage() != null){ //프로필 이미지 변경 됐으면 세션 수정
                if (member.getProfileImage().equals("")) {
                    user.setProfileImage("none");
                } else {
                    user.setProfileImage(member.getProfileImage());
                }
            }
            if(member.getPassword() != null)  //비밀번호 변경 됐으면 세션 수정
                user.setPassword(member.getPassword());
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String findIdByEmail(String email) {
        return memberDao.searchIdByEmail(email);
    }

    @Override
    public int searchMember(String id, String email) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("email", email);
        return memberDao.searchMember(map);
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
}