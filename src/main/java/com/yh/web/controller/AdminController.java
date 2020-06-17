package com.yh.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LEADER')")
	@GetMapping(path = {"", "/"})
	public String getIndex() {
		return "admin/index";
	}
}
