package com.yh.web.security;

import com.yh.web.dto.Member;
import com.yh.web.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberService memberService;

    @Autowired
    public CustomUserDetailsService(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public UserDetails loadUserByUsername(String id) {//유저정보를 읽어오는 역할
        // Id에 해당하는 정보를 데이터베이스에서 읽어 Member객체에 저장한다.
        // 해당 정보를 CustomUserDetails객체에 저장한다.
        Member member = memberService.getMemberInfor(id);
        if (member == null) { //존재하지 않은 아이디인 경우 여기서 리턴
            throw new UsernameNotFoundException("사용자가 입력한 아이디에 해당하는 사용자를 찾을 수 없습니다.");
        }

        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setUsername(member.getId());
        customUserDetails.setPassword(member.getPasswd());
        customUserDetails.setProfileImage(member.getProfileImage());

        customUserDetails.setEnabled(member.isEnable());
        customUserDetails.setAccountNonExpired(true);
        customUserDetails.setAccountNonLocked(true);
        customUserDetails.setCredentialsNonExpired(true);
        return customUserDetails;
    }


}