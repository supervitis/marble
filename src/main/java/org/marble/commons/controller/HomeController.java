package org.marble.commons.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import org.marble.commons.model.HomeInformation;
import org.marble.commons.service.InformationService;

@Controller
public class HomeController {

    @Autowired
    InformationService informationService;
    
	@RequestMapping({ "/", "/home" })
	public ModelAndView home() {
		HomeInformation homeInformation = informationService.getHomeInformation();
		ModelAndView modelAndView = new ModelAndView("home");
		modelAndView.addObject("info", homeInformation);
		return modelAndView;
	}

}