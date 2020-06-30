package com.yh.web.controller;

import com.yh.web.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;

@Slf4j
@Controller
public class HomeController {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView mav = new ModelAndView("/index");
        log.info("메인페이지");
        mav.addObject("page_title", "YH");
        return mav;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/para")
    public @ResponseBody Principal haha(Principal principal) {
        return principal;
    }

    @GetMapping(path = "/expired")
    public void expired(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*RequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest(request, response); //클라이언트 요청명
        log.info("{}",savedRequest);
        if (savedRequest == null) {
            response.sendRedirect(Utils.getRoot() + "/index");
        }*/
        response.setContentType("text/html;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        String msg = "<script>" +
                "        alert('다른 사용자가 접속하여 로그인이 해제되었습니다.');" +
                "        location.href= ' " + Utils.getRoot() + "/index';" +
                "    </script>";
        out.write(msg);
    }



}
