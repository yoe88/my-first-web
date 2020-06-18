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

    @Autowired
    public CustomAuthenticationProvider(PasswordEncoder passwordEncoder
            , CustomUserDetailsService customUserDetailsService) {
        logger.info("CustomAuthenticationProvider Init");
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailsService = customUserDetailsService;
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
