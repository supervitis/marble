package org.marble.commons.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login() {
		return new ModelAndView("login");
	}

	@RequestMapping(value = "/login/failure", method = RequestMethod.GET)
	// Check
	public ModelAndView loginerror() {
		ModelAndView model = new ModelAndView("login");
		model.addObject("error", "true");
		return model;
	}
	
	@RequestMapping(value = "/login/promote", method = RequestMethod.GET)
	// Check
	public ModelAndView loginpromote() {
		ModelAndView model = new ModelAndView("login");
		model.addObject("promote", "true");
		return model;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logout(Model model) {
		return new ModelAndView("home");
	}
}