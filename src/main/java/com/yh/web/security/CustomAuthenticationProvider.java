package com.yh.web.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
//로그인 인증관리
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public CustomAuthenticationProvider(PasswordEncoder passwordEncoder
            , CustomUserDetailsService customUserDetailsService) {
        log.info("CustomAuthenticationProvider Init");
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication == null) {
            throw new InternalAuthenticationServiceException("Authentication is null");
        }
        String username = authentication.getName();
        log.info("사용자가 입력한 아이디 : " + username);
        if (authentication.getCredentials() == null) {
            throw new AuthenticationCredentialsNotFoundException("Credentials is null");
        }
        String password = authentication.getCredentials().toString();
        log.info("사용자가 입력한 비밀번호 : " + password);

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
        log.info("인증완료");

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
