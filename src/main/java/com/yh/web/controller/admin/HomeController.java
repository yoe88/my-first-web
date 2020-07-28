package com.yh.web.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller("admin.homeController")
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class HomeController {

	/**
	 * @return 관리자 페이지
	 */
	@GetMapping(path = {"/index",""})
	public ModelAndView index() {
		ModelAndView mav = new ModelAndView("/admin/index");
		mav.addObject("page_title", "관리자");
		return mav;
	}
}
