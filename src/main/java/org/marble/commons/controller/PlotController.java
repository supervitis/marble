package org.marble.commons.controller;

import java.util.List;

import org.marble.commons.dao.model.Plot;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.exception.InvalidPlotException;
import org.marble.commons.exception.InvalidTopicException;
import org.marble.commons.service.PlotService;
import org.marble.commons.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

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
        modelAndView.setViewName("plot_list");
        modelAndView.addObject("plots", plots);
        modelAndView.addObject("topic", topic);
        return modelAndView;
    }
}