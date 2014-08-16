package org.marble.commons.controller;

import org.marble.commons.exception.InvalidUserException;
import org.marble.commons.model.SignupForm;
import org.marble.commons.service.ResetServiceImpl;
import org.marble.commons.service.SampleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


/**
 * User: ryan
 * Date: 2/7/13
 */
@Controller
public class SignupController {

    private final String PAGE_INDEX = "signup";
    private final String PAGE_SHOW = "show";

	private static final Logger log =  LoggerFactory.getLogger(ResetServiceImpl.class);
	
    @Autowired
    SampleService sampleService;


    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public ModelAndView index() {
        return new ModelAndView(PAGE_INDEX, "signupForm", new SignupForm());
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String search(Model model, @Valid SignupForm signupForm, BindingResult result, RedirectAttributes redirectAttributes) {

        String returnPage = PAGE_INDEX;

        if (!result.hasErrors()) {
            try {
                model.addAttribute("form", sampleService.saveFrom(signupForm));
                returnPage = PAGE_SHOW;
            } catch (InvalidUserException e) {
            	log.error("MFC error: " + e.getMessage());
                model.addAttribute("page_error", e.getMessage());
            }
        }
        return returnPage;
    }

    @RequestMapping(value = "/security-error", method = RequestMethod.GET)
    public String securityError(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("page_error", "You do have have permission to do that!");
        return "redirect:/";
    }

}


