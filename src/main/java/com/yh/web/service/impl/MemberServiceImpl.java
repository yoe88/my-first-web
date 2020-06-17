package com.yh.web.service.impl;

import com.yh.web.dao.MemberDao;
import com.yh.web.dto.Member;
import com.yh.web.dto.MemberRole;
import com.yh.web.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("memberService")
public class MemberServiceImpl implements MemberService {
    final MemberDao memberDao;
    final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberServiceImpl(MemberDao memberDao, PasswordEncoder passwordEncoder) {
        this.memberDao = memberDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    @Override
    public Member getMemberInfor(String id) {
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

    @Transactional
    @Override
    public void updateMember() {
        memberDao.updateMember();
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
        member.setPasswd(passwordEncoder.encode(member.getPasswd()));
        int result = 0;
        result += memberDao.insertMember(member);
        result += memberDao.insertMemberRole(member.getId());
        return  result;
    }
}