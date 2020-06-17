package com.yh.web.security;

import com.yh.web.dto.MemberRole;
import com.yh.web.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
//로그인 인증관리
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    private final MemberService memberService;

    @Autowired
    public CustomAuthenticationProvider(PasswordEncoder passwordEncoder, CustomUserDetailsService customUserDetailsService
                                    ,MemberService memberService) {
        logger.info("인증설정");
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailsService = customUserDetailsService;
        this.memberService = memberService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication == null) {
            throw new InternalAuthenticationServiceException("Authentication is null");
        }
        String username = authentication.getName();
        logger.info("사용자가 입력한 아이디 : " + username);
        if (authentication.getCredentials() == null) {
            throw new AuthenticationCredentialsNotFoundException("Credentials is null");
        }
        String password = authentication.getCredentials().toString();
        logger.info("사용자가 입력한 비밀번호 : " + password);

        CustomUserDetails user = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);
        //계정 잠금여부
        if (!user.isAccountNonLocked()) {
            throw new LockedException("User account is locked");
        }
        //계정 활성
        if (!user.isEnabled()) {
            throw new DisabledException("User is disabled");
        }
        //계정만료
        if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException("User account has expired");
        }
        //입력한 비밀번호 비교
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Password does not match stored value");
        }
        // 인증만료
        if (!user.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("User credentials have expired");
        }
        logger.info("인증완료");
        ///인증이 되었으면 ID에 해당하는 권한정보를 가져와 저장한다.
        List<MemberRole> memberRoles = memberService.getMemberRoles(user.getUsername());
        // 로그인 한 사용자의 권한 정보를 GrantedAuthority를 구현하고 있는 SimpleGrantedAuthority객체에 담아
        // 리스트에 추가한다. MemberRole 이름은 "ROLE_"로 시작해야 한다
        List<GrantedAuthority> authorities = new ArrayList<>();
        if(memberRoles != null) {
            for (MemberRole memberRole : memberRoles) {
                authorities.add(new SimpleGrantedAuthority(memberRole.getRoleName()));
            }
        }
        user.setAuthorities(authorities);
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
        //return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
