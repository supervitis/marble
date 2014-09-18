package org.marble.commons.controller;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.marble.commons.dao.model.Plot;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.exception.InvalidExecutionException;
import org.marble.commons.exception.InvalidModuleException;
import org.marble.commons.exception.InvalidPlotException;
import org.marble.commons.exception.InvalidPlotParametersException;
import org.marble.commons.exception.InvalidTopicException;
import org.marble.commons.model.ExecutionCreationParameters;
import org.marble.commons.model.PlotModule;
import org.marble.commons.service.ExecutionService;
import org.marble.commons.service.ModuleService;
import org.marble.commons.service.PlotService;
import org.marble.commons.service.PlotServiceImpl;
import org.marble.commons.service.TopicService;
import org.marble.commons.util.MarbleUtil;

import org.apache.velocity.exception.ParseErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/plot")
public class PlotController {

    private static final Logger log = LoggerFactory.getLogger(PlotController.class);

    @Autowired
    PlotService plotService;

    @Autowired
    TopicService topicService;

    @Autowired
    ModuleService modulesService;

    @Autowired
    ExecutionService executionService;

    @RequestMapping(value = "/{plotId:[0-9]+}", method = RequestMethod.GET)
    public ModelAndView view(@PathVariable Integer plotId) throws InvalidPlotException {
        ModelAndView modelAndView = new ModelAndView();
        Plot plot;
        plot = plotService.findOne(plotId);
        modelAndView.setViewName("plot_view");
        modelAndView.addObject("plot", plot);
        return modelAndView;
    }

    @RequestMapping(value = "/topic/{topicId:[0-9]+}", method = RequestMethod.GET)
    public ModelAndView showPerTopic(@PathVariable Integer topicId) throws InvalidPlotException,
            InvalidTopicException {
        ModelAndView modelAndView = new ModelAndView();

        Topic topic = topicService.findOne(topicId);
        List<Plot> plots;
        plots = plotService.findByTopic(topicId);
        modelAndView.setViewName("plots_list");
        modelAndView.addObject("plots", plots);
        modelAndView.addObject("topic", topic);
        return modelAndView;
    }

    @RequestMapping(value = "/delete/{plotId:[0-9]+}")
    public String delete(@PathVariable Integer plotId, RedirectAttributes redirectAttributes,
            HttpServletRequest request)
            throws InvalidPlotException {
        String basePath = MarbleUtil.getBasePath(request);
        Integer topicId = plotService.findOne(plotId).getTopic().getId();
        plotService.delete(plotId);
        // Setting message
        redirectAttributes.addFlashAttribute("notificationMessage", "PlotController.plotDeleted");
        redirectAttributes.addFlashAttribute("notificationIcon", "fa-check-circle");
        redirectAttributes.addFlashAttribute("notificationLevel", "success");
        return "redirect:" + basePath + "/topic/" + topicId + "/plot";
    }

    @RequestMapping(value = "/topic/{topicId:[0-9]+}/create", method = RequestMethod.GET)
    public ModelAndView create(@PathVariable Integer topicId) throws InvalidPlotException, InvalidTopicException {
        ModelAndView modelAndView = new ModelAndView();

        Topic topic = topicService.findOne(topicId);
        List<PlotModule> modules = modulesService.getPlotterModules();
        modelAndView.setViewName("plot_create");
        modelAndView.addObject("modules", modules);
        modelAndView.addObject("topic", topic);
        return modelAndView;
    }

    @RequestMapping(value = "/topic/{topicId:[0-9]+}/create", method = RequestMethod.POST)
    public String createResponse(@PathVariable Integer topicId, @ModelAttribute ExecutionCreationParameters formInput,
            RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String basePath = MarbleUtil.getBasePath(request);

        Integer executionId;
        try {
            executionId = executionService.executePlotter(topicId, formInput);
        } catch (InvalidTopicException | InvalidExecutionException | InvalidPlotParametersException
                | InvalidModuleException e) {
            redirectAttributes.addFlashAttribute("notificationMessage", "AdminController.senticDataUploadError");
            redirectAttributes.addFlashAttribute("notificationIcon", "fa-exclamation-triangle");
            redirectAttributes.addFlashAttribute("notificationLevel", "danger");
            return "redirect:" + basePath + "/topic/" + topicId + "/plot/create";
        }

        redirectAttributes.addFlashAttribute("notificationMessage", "AdminController.senticDataUploaded");
        redirectAttributes.addFlashAttribute("notificationIcon", "fa-check-circle");
        redirectAttributes.addFlashAttribute("notificationLevel", "success");
        return "redirect:" + basePath + "/execution/" + executionId;
    }
}