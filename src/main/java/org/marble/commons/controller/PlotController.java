package org.marble.commons.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.marble.commons.dao.model.Plot;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.exception.InvalidPlotException;
import org.marble.commons.exception.InvalidTopicException;
import org.marble.commons.model.PlotModule;
import org.marble.commons.service.PlotService;
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
@RequestMapping("/plot")
public class PlotController {

    @Autowired
    PlotService plotService;

    @Autowired
    TopicService topicService;

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
    public ModelAndView create(@PathVariable Integer topicId) throws InvalidPlotException {
        ModelAndView modelAndView = new ModelAndView();
        List<PlotModule> modules = plotService.getModules();
        modelAndView.setViewName("plot_create");
        modelAndView.addObject("modules", modules);
        return modelAndView;
    }
}