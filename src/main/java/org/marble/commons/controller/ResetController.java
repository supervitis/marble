package org.marble.commons.controller;

import org.marble.commons.service.ResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/*
 * Only for reset purposes. It will be removed on future versions.
 */
@Controller
@RequestMapping(value = "/admin/reset")
public class ResetController {

	@Autowired
	ResetService resetService;

	@RequestMapping(value = "/rebase", method = RequestMethod.GET)
	public String rebase(RedirectAttributes redirectAttributes) {
		// Reseting the data
		resetService.resetAll();
		
		// Setting message
		redirectAttributes.addFlashAttribute("notificationMessage", "ResetController.rebaseMessage");
		redirectAttributes.addFlashAttribute("notificationLevel", "danger");
		return "redirect:/";
	}
}