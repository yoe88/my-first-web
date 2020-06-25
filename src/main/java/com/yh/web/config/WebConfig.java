package com.yh.web.config;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;


import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

//web.xml을 대신한다.
public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer {
    // Spring Config 파일을 설정한다.
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{RootConfig.class, SecurityConfig.class, MailConfig.class };
    }

    // Spring MVC(WEB) Config 파일을 설정한다. MVC Config는 Bean을 RootConfig에서 설정한 곳에서부터 찾는다.
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{MvcConfig.class};
    }

    // DispatcherServlet이 매핑되기 위한 경로를 지정한다.
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

   
    //인코딩 필터 설정
    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");

        return new Filter[]{encodingFilter, new HiddenHttpMethodFilter()};
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
    }
}