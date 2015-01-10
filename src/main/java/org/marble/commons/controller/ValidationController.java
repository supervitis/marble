package org.marble.commons.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.marble.commons.exception.InvalidExecutionException;
import org.marble.commons.exception.InvalidModuleException;
import org.marble.commons.exception.InvalidTopicException;
import org.marble.commons.model.ExecutionModuleDefinition;
import org.marble.commons.model.ExecutionModuleParameters;
import org.marble.commons.service.ExecutionService;
import org.marble.commons.service.ModuleService;
import org.marble.commons.util.MarbleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/*
 * Only for reset purposes. It will be removed on future versions.
 */
@Controller
@RequestMapping(value = "/validation")
public class ValidationController {

    @Autowired
    ExecutionService executionService;

    @Autowired
    ModuleService modulesService;

    @Autowired
    private Validator validator;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView validateRequest(RedirectAttributes redirectAttributes, HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView();

        // Setting message
        List<ExecutionModuleDefinition> modules = modulesService.getProcessorModules();
        modelAndView.setViewName("process_execute");
        modelAndView.addObject("modules", modules);

        modelAndView.setViewName("validation");
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String validateResponse(@Valid ExecutionModuleParameters moduleParameters, BindingResult result,
            RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String basePath = MarbleUtil.getBasePath(request);

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("notificationMessage", "ProcessController.executeProcessorError");
            redirectAttributes.addFlashAttribute("notificationIcon", "fa-exclamation-triangle");
            redirectAttributes.addFlashAttribute("notificationLevel", "danger");
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.executionModuleParameters", result);
            return "redirect:" + basePath + "/validation";
        }

        Integer executionId;
        try {
            executionId = executionService.executeProcessor(moduleParameters);
        } catch (InvalidTopicException | InvalidExecutionException | InvalidModuleException e) {
            redirectAttributes.addFlashAttribute("notificationMessage", "ProcessController.processorExecutionFailed");
            redirectAttributes.addFlashAttribute("notificationIcon", "fa-exclamation-triangle");
            redirectAttributes.addFlashAttribute("notificationLevel", "danger");
            return "redirect:" + basePath + "/validation";
        }

        redirectAttributes.addFlashAttribute("notificationMessage", "ProcessController.processorExecuted");
        redirectAttributes.addFlashAttribute("notificationIcon", "fa-check-circle");
        redirectAttributes.addFlashAttribute("notificationLevel", "success");
        return "redirect:" + basePath + "/execution/" + executionId;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
        // You can register other Custom Editors with the WebDataBinder, like
        // CustomNumberEditor for Integers and Longs,
        // or StringTrimmerEditor for Strings
    }

}