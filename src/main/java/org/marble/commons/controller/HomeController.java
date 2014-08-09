package org.marble.commons.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

  @RequestMapping("/")
  public ModelAndView home() { 
    //model.addAttribute("greeting", "Welcome to Web Store!");
    ModelAndView modelAndView = new ModelAndView ("home");
    return modelAndView;
  }

}