package org.marble.commons.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.marble.commons.dao.model.Plot;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.exception.InvalidExecutionException;
import org.marble.commons.exception.InvalidModuleException;
import org.marble.commons.exception.InvalidPlotException;
import org.marble.commons.exception.InvalidTopicException;
import org.marble.commons.model.ExecutionModuleParameters;
import org.marble.commons.model.ExecutionModuleDefinition;
import org.marble.commons.service.ExecutionService;
import org.marble.commons.service.ModuleService;
import org.marble.commons.service.PlotService;
import org.marble.commons.service.TopicService;
import org.marble.commons.util.MarbleUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/process")
public class ProcessController {

    private static final Logger log =
            LoggerFactory.getLogger(ProcessController.class);

    @Autowired
    TopicService topicService;

    @Autowired
    ModuleService moduleService;

    @Autowired
    ExecutionService executionService;
    
    @Autowired
    private Validator validator; 

    @RequestMapping(value = "/topic/{topicId:[0-9]+}/execute", method = RequestMethod.GET)
    public ModelAndView create(@PathVariable Integer topicId) throws InvalidPlotException, InvalidTopicException {
        ModelAndView modelAndView = new ModelAndView();

        Topic topic = topicService.findOne(topicId);
        List<ExecutionModuleDefinition> modules = moduleService.getProcessorModules();
        modelAndView.setViewName("process_execute");
        modelAndView.addObject("modules", modules);
        modelAndView.addObject("topic", topic);
        return modelAndView;
    }

    @RequestMapping(value = "/topic/{topicId:[0-9]+}/execute", method = RequestMethod.POST)
    public String createResponse(@Valid ExecutionModuleParameters moduleParameters,
            BindingResult result, @PathVariable Integer topicId, RedirectAttributes redirectAttributes,
            HttpServletRequest request) {
        String basePath = MarbleUtil.getBasePath(request);

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("notificationMessage", "ProcessController.executeProcessorError");
            redirectAttributes.addFlashAttribute("notificationIcon", "fa-exclamation-triangle");
            redirectAttributes.addFlashAttribute("notificationLevel", "danger");
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.executionModuleParameters", result);
            return "redirect:" + basePath + "/topic/" + topicId + "/process/execute";
        }

        Integer executionId;
        try {
            executionId = executionService.executeProcessor(topicId, moduleParameters);
        } catch (InvalidTopicException | InvalidExecutionException | InvalidModuleException e) {
            redirectAttributes.addFlashAttribute("notificationMessage", "ProcessController.processorExecutionFailed");
            redirectAttributes.addFlashAttribute("notificationIcon", "fa-exclamation-triangle");
            redirectAttributes.addFlashAttribute("notificationLevel", "danger");
            return "redirect:" + basePath + "/topic/" + topicId + "/process/execute";
        }

        redirectAttributes.addFlashAttribute("notificationMessage", "ProcessController.processorExecuted");
        redirectAttributes.addFlashAttribute("notificationIcon", "fa-check-circle");
        redirectAttributes.addFlashAttribute("notificationLevel", "success");
        return "redirect:" + basePath + "/execution/" + executionId;
    }
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        sdf.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
        binder.setValidator(validator);
        // You can register other Custom Editors with the WebDataBinder, like
        // CustomNumberEditor for Integers and Longs,
        // or StringTrimmerEditor for Strings
    }
}