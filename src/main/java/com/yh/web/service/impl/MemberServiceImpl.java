package com.yh.web.service.impl;

import com.yh.web.dao.MemberDao;
import com.yh.web.dto.Member;
import com.yh.web.dto.MemberRole;
import com.yh.web.security.CustomUserDetails;
import com.yh.web.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service("memberService")
public class MemberServiceImpl implements MemberService {
    final MemberDao memberDao;
    final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberServiceImpl(MemberDao memberDao, PasswordEncoder passwordEncoder) {
        log.info("MemberServiceImpl Init...");
        this.memberDao = memberDao;
        this.passwordEncoder = passwordEncoder;
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
    public String searchId(String id) {
        return memberDao.selectId(id);
    }

    @Transactional(readOnly = true)
    @Override
    public String searchEmail(String email) {
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
}