package com.yh.web.config;

import com.yh.web.security.CustomAuthenticationFailureHandler;
import com.yh.web.security.CustomAuthenticationSuccessHandler;
import com.yh.web.security.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity //@EnableWebSecurity가 붙어 있을 경우 스프링 시큐리티를 구성하는 기본적인 빈(Bean)들을 자동으로 구성해줍니다.
//@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) //default false
@ComponentScan(basePackages = {"com.yh.web.security"})
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AuthenticationProvider customAuthenticationProvider; //로그인인증
    private final CustomUserDetailsService customUserDetailsService;
    private final DataSource dataSource;

    @Autowired  //순환 참조 문제 빈의 초기화를 늦추는작업, 빈을 미리 만들어 놓지 않고 필요로 하는시점에 만든다.
    public SecurityConfig(@Lazy AuthenticationProvider customAuthenticationProvider,
                          @Lazy CustomUserDetailsService customUserDetailsService,
                          DataSource dataSource) {
        logger.info("시큐리티 설정");
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.customUserDetailsService = customUserDetailsService;
        this.dataSource = dataSource;
    }


    //   resources 경로에 대한 요청은 인증/인가 처리하지 않도록 한다.
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/resources/**");
    }

    //   /, /index에 대한 요청은 누구나 할 수 있지만, 
    // 그 외의 요청은 모두 인증 후 접근 가능합니다.
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
        .csrf().disable() //토근 403
        .authorizeRequests()  //인증 요청 설정 (패턴, 접근허용) @PreAuthorize로 하였음
        //.antMatchers("/", "/index", "/boards").permitAll() 해당하는 요청은 로그인 안해도 전부 허용
        //.antMatchers("/admin/**").hasRole("ADMIN")
        //.antMatchers("/boards/**", "/members/**").hasAnyRole("ADMIN","USER")
        //.anyRequest().authenticated()  //설정한 요청 이외의 요청은 인증된 사용자만이 요청 가능
        .and()
            .formLogin()  //로그인관련 설정
            .loginPage("/member/login")   //로그인페이지로 가는 요청
            .usernameParameter("id")
            .passwordParameter("passwd")
            .loginProcessingUrl("/member/loginProcess")  //로그인 인증
            //.failureForwardUrl("/members/login")    //로그인 실패시 이동할 페이지 forward방식
            //.failureUrl("/members/login?login_error=1") //로그인 실패시 이동할 페이지 Redirect방식
            .failureHandler(customAuthenticationFailureHandler())
            //.defaultSuccessUrl("/",true) 로그인 성공후 이동할 페이지
            .successHandler(customAuthenticationSuccessHandler())
            .permitAll()
        .and()
            .logout()
            .logoutUrl("/logout")     //로그아웃 요청 url
            .logoutSuccessUrl("/")     //로그아웃이 됐으면 이동할 url
            .invalidateHttpSession(true)   //로그아웃시 세션전부제거
        .and()
       		.exceptionHandling()
       		.accessDeniedPage("/WEB-INF/views/commons/error/error403.jsp")  //권한없는 요청시
        .and()
            // http.rememberMe()에 userDetailsService(DB에서 유저 정보를 가져오는 역할)의 구현체와
            // JdbcTokenRepositoryImpl(dataSource)타입을 값으로 준다.
            .rememberMe()
            .userDetailsService(customUserDetailsService)
            .tokenRepository(tokenRepository()); //username, 시리즈, 토근 정보를 DB에 저장
    }

    //내가 만든 인증 시스템인 CustomAuthenticationProvider를 ProviderManager가 알 수 있게 ProviderManager에게 등록해줘야한다.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(customAuthenticationProvider);
    }

    /**
     * @return 로그인실패
     */
    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    /**
     * @return 로그인성공
     */
    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler(){
        return new CustomAuthenticationSuccessHandler();
    }

    // tokenRepository의 구현체
    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    // 패스워드 인코더를 빈으로 등록. 암호를 인코딩하거나,
    // 인코딩된 암호와 사용자가 입력한 암호가 같은 지 확인할 때 사용한다.
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}