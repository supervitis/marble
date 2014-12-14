package org.marble.commons.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

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
import org.marble.commons.service.ValidationDataService;
import org.marble.commons.util.MarbleUtil;

@Controller
@RequestMapping("/admin")
public class DataUploadController {

    // private static final Logger log =
    // LoggerFactory.getLogger(AdminController.class);

    @Autowired
    SenticNetService senticNetService;
    
    @Autowired
    ValidationDataService validationDataService;

    @RequestMapping(value = "/upload/sentic", method = RequestMethod.POST)
    public String uploadSentic(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        String basePath = MarbleUtil.getBasePath(request);
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
    
    @RequestMapping(value = "/upload/validationData", method = RequestMethod.POST)
    public String uploadValidationData(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes,
            HttpServletRequest request) {

        String basePath = MarbleUtil.getBasePath(request);
        try {
            validationDataService.insertDataFromFile(file);
        } catch (IllegalStateException | IOException | SAXException | ParserConfigurationException e) {
            redirectAttributes.addFlashAttribute("notificationMessage", "AdminController.validationDataUploadError");
            redirectAttributes.addFlashAttribute("notificationIcon", "fa-exclamation-triangle");
            redirectAttributes.addFlashAttribute("notificationLevel", "danger");
            return "redirect:" + basePath + "/admin";
        }

        redirectAttributes.addFlashAttribute("notificationMessage", "AdminController.validationDataUploaded");
        redirectAttributes.addFlashAttribute("notificationIcon", "fa-check-circle");
        redirectAttributes.addFlashAttribute("notificationLevel", "success");
        return "redirect:" + basePath + "/admin";
    }

}