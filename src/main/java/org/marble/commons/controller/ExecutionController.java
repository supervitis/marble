package org.marble.commons.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.marble.commons.dao.model.Execution;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.exception.InvalidExecutionException;
import org.marble.commons.exception.InvalidTopicException;
import org.marble.commons.service.ExecutionService;
import org.marble.commons.service.TopicService;
import org.marble.commons.util.MarbleUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/execution")
public class ExecutionController {

    @Autowired
    ExecutionService executionService;

    @Autowired
    TopicService topicService;

    @RequestMapping(value = "/{executionId:[0-9]+}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable Integer executionId) throws InvalidExecutionException {
        ModelAndView modelAndView = new ModelAndView();

        Execution execution;
        execution = executionService.findOne(executionId);
        modelAndView.addObject("topic", execution.getTopic());
        modelAndView.setViewName("execution_view");
        modelAndView.addObject("execution", execution);
        return modelAndView;
    }

    @RequestMapping(value = "/topic/{topicId:[0-9]+}", method = RequestMethod.GET)
    public ModelAndView showPerTopic(@PathVariable Integer topicId) throws InvalidExecutionException,
            InvalidTopicException {
        ModelAndView modelAndView = new ModelAndView();

        Topic topic = topicService.findOne(topicId);
        List<Execution> executions;
        executions = executionService.getExecutionsPerTopic(topicId);
        modelAndView.setViewName("executions_list");
        modelAndView.addObject("executions", executions);
        modelAndView.addObject("topic", topic);
        return modelAndView;
    }

    @RequestMapping(value = "/topic/{topicId:[0-9]+}/extract", method = RequestMethod.GET)
    public String executeExtractor(@PathVariable Integer topicId, RedirectAttributes redirectAttributes,
            HttpServletRequest request) {
        String basePath = MarbleUtil.getBasePath(request);
        Integer executionId = 0;
        try {
            executionId = executionService.executeExtractor(topicId);
        } catch (InvalidExecutionException | InvalidTopicException e) {
            // Setting message
            redirectAttributes.addFlashAttribute("notificationMessage", "ExecutionController.extractorExecutionFailed");
            redirectAttributes.addFlashAttribute("notificationIcon", "fa-exclamation-triangle");
            redirectAttributes.addFlashAttribute("notificationLevel", "danger");
            return "redirect:" + basePath + "topic/" + topicId + "/execution";
        }

        // Setting message
        redirectAttributes.addFlashAttribute("notificationMessage", "ExecutionController.extractorExecuted");
        redirectAttributes.addFlashAttribute("notificationIcon", "fa-sign-in");
        redirectAttributes.addFlashAttribute("notificationLevel", "success");
        return "redirect:" + basePath + "/execution/" + executionId;
    }
}