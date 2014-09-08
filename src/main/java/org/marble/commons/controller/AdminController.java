package org.marble.commons.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.SAXException;

import org.marble.commons.service.SenticNetService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    SenticNetService senticNetService;

    @RequestMapping
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("admin");
        return modelAndView;
    }

    // TODO MFC: Move to some other place
    @RequestMapping(value = "/upload/sentic", method = RequestMethod.POST)
    public String uploadSentic(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        try {
            senticNetService.insertDataFromFile(file);
        } catch (IllegalStateException | IOException | SAXException | ParserConfigurationException e) {
            redirectAttributes.addFlashAttribute("notificationMessage", "AdminController.senticDataUploadError");
            redirectAttributes.addFlashAttribute("notificationIcon", "fa-exclamation-triangle");
            redirectAttributes.addFlashAttribute("notificationLevel", "danger");
            return "redirect:" + basePath + "/admin";
        }

        redirectAttributes.addFlashAttribute("notificationMessage", "AdminController.senticDataUploaded");
        redirectAttributes.addFlashAttribute("notificationIcon", "fa-check-circle");
        redirectAttributes.addFlashAttribute("notificationLevel", "success");
        return "redirect:" + basePath + "/admin";
    }

}