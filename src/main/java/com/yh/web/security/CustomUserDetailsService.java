package com.yh.web.security;

import com.yh.web.dto.Member;
import com.yh.web.dto.MemberRole;
import com.yh.web.service.MemberService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberService memberService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public CustomUserDetailsService(MemberService memberService) {
        logger.info("CustomUserDetailsService Init");
        this.memberService = memberService;
    }

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String id) {//유저정보를 읽어오는 역할
        // Id에 해당하는 정보를 데이터베이스에서 읽어 Member객체에 저장한다.
        // 해당 정보를 CustomUserDetails객체에 저장한다.
        Member member = memberService.getMemberInfo(id);
        if (member == null) { //존재하지 않은 아이디인 경우 여기서 리턴
            throw new UsernameNotFoundException("사용자가 입력한 아이디에 해당하는 사용자를 찾을 수 없습니다.");
        }
        logger.info(member.toString());

        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setUsername(member.getId());
        customUserDetails.setPassword(member.getPassword());
        if(member.getProfileImage() == null){
            member.setProfileImage("none");
        }else{
            member.setProfileImage(URLEncoder.encode(member.getProfileImage(),"UTF-8").replace("+","%20"));
        }
        customUserDetails.setProfileImage(member.getProfileImage());

        customUserDetails.setEnabled(member.isEnable());
        customUserDetails.setAccountNonExpired(true);
        customUserDetails.setAccountNonLocked(true);
        customUserDetails.setCredentialsNonExpired(true);

        ///ID에 해당하는 권한정보를 가져와 저장한다.
        List<MemberRole> memberRoles = memberService.getMemberRoles(customUserDetails.getUsername());
        // 로그인 한 사용자의 권한 정보를 GrantedAuthority를 구현하고 있는 SimpleGrantedAuthority객체에 담아
        // 리스트에 추가한다. MemberRole 이름은 "ROLE_"로 시작해야 한다
        List<GrantedAuthority> authorities = new ArrayList<>();
        if(memberRoles != null) {
            for (MemberRole memberRole : memberRoles) {
                authorities.add(new SimpleGrantedAuthority(memberRole.getRoleName()));
            }
        }
        customUserDetails.setAuthorities(authorities);

        logger.info("success setting customUser");
        return customUserDetails;
    }
}