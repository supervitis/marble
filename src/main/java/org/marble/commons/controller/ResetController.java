package org.marble.commons.controller;

import javax.servlet.http.HttpServletRequest;

import org.marble.commons.service.ResetService;
import org.marble.commons.util.MarbleUtil;

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
    public String rebase(RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String basePath = MarbleUtil.getBasePath(request);
        // Reseting the data
        resetService.resetAll();

        // Setting message
        redirectAttributes.addFlashAttribute("notificationMessage", "ResetController.rebaseMessage");
        redirectAttributes.addFlashAttribute("notificationIcon", "fa-exclamation-triangle");
        redirectAttributes.addFlashAttribute("notificationLevel", "danger");
        return "redirect:" + basePath + "/";
    }

    /*
    @RequestMapping(value = "/special", method = RequestMethod.GET)
    public String special(RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String basePath = MarbleUtil.getBasePath(request);
        // Reseting the data
        resetService.getTheSpecial();

        // Setting message
        redirectAttributes.addFlashAttribute("notificationMessage", "ResetController.specialMessage");
        redirectAttributes.addFlashAttribute("notificationIcon", "fa-exclamation-triangle");
        redirectAttributes.addFlashAttribute("notificationLevel", "success");
        return "redirect:" + basePath + "/admin";
    }*/
}