package com.startupoxygen.craft.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/helloworld")
public class CraftHelloWorldController {

	@RequestMapping(value = "{argProjectContextPath}", method = RequestMethod.GET)
	public String displayLayout(Model argModel,
			@PathVariable String argProjectContextPath,
			HttpServletRequest argRequest, HttpServletResponse argResponse) {
		if (argProjectContextPath != null && !argProjectContextPath.isEmpty()) {
			argModel.addAttribute("contextPath", argProjectContextPath);
		} else {
			argModel.addAttribute("contextPath", "ROOT");
		}
		return "helloworld";
	}

}
