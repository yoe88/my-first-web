package com.yh.web.controller;

import com.yh.web.dao.impl.MemberDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    MemberDaoImpl service;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView home() {
        ModelAndView mav = new ModelAndView("/index");
        logger.info("메인페이지");
        mav.addObject("page_title", "YH");
        return mav;
    }
 
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/para")
    public @ResponseBody
    Principal haha(Principal principal) {
        return principal;
    }


}
