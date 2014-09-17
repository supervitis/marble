package org.marble.commons.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.marble.commons.dao.model.Topic;
import org.marble.commons.exception.InvalidTopicException;
import org.marble.commons.model.TopicInfo;
import org.marble.commons.service.TopicService;
import org.marble.commons.util.MarbleUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/topic")
public class TopicController {

    // private static final Logger log =
    // LoggerFactory.getLogger(TopicController.class);

    @Autowired
    TopicService topicService;

    @Autowired
    private Validator validator;

    @RequestMapping
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("topics_list");
        modelAndView.addObject("topics", topicService.findAll());
        return modelAndView;
    }

    @RequestMapping(value = "/edit/{topicId:[0-9]+}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable Integer topicId) throws InvalidTopicException {
        ModelAndView modelAndView = new ModelAndView();

        Topic topic;
        topic = topicService.findOne(topicId);
        modelAndView.setViewName("topic_edit");
        modelAndView.addObject("topic", topic);
        return modelAndView;
    }

    @RequestMapping(value = "/{topicId:[0-9]+}", method = RequestMethod.GET)
    public ModelAndView info(@PathVariable Integer topicId) throws InvalidTopicException {
        ModelAndView modelAndView = new ModelAndView();

        TopicInfo topicInfo;
        topicInfo = topicService.info(topicId);
        modelAndView.setViewName("topic_info");
        modelAndView.addObject("topicInfo", topicInfo);
        return modelAndView;
    }

    @RequestMapping(value = "/edit/{topicId:[0-9]+}", method = RequestMethod.POST)
    public ModelAndView save(@Valid Topic topic, BindingResult result, @PathVariable Integer topicId,
            RedirectAttributes redirectAttributes, HttpServletRequest request)
            throws InvalidTopicException {

        String basePath = MarbleUtil.getBasePath(request);
        ModelAndView modelAndView = new ModelAndView();

        if (result.hasErrors()) {
            modelAndView.addObject("notificationMessage", "TopicController.editTopicError");
            modelAndView.addObject("notificationIcon", "fa-exclamation-triangle");
            modelAndView.addObject("notificationLevel", "danger");
            modelAndView.setViewName("topic_edit");
            modelAndView.addObject("topic", topic);
            return modelAndView;
        }

        topicService.save(topic);

        redirectAttributes.addFlashAttribute("notificationMessage", "TopicController.topicModified");
        redirectAttributes.addFlashAttribute("notificationIcon", "fa-check-circle");
        redirectAttributes.addFlashAttribute("notificationLevel", "success");
        modelAndView.setViewName("redirect:" + basePath + "/topic/");
        return modelAndView;
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create() throws InvalidTopicException {
        ModelAndView modelAndView = new ModelAndView();

        Topic topic = new Topic();
        modelAndView.setViewName("topic_create");
        modelAndView.addObject("topic", topic);
        return modelAndView;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(@ModelAttribute("topic") @Valid Topic topic, BindingResult result,
            RedirectAttributes redirectAttributes, HttpServletRequest request)
            throws InvalidTopicException {

        String basePath = MarbleUtil.getBasePath(request);
        ModelAndView modelAndView = new ModelAndView();

        if (result.hasErrors()) {
            modelAndView.addObject("notificationMessage", "TopicController.addTopicError");
            modelAndView.addObject("notificationIcon", "fa-exclamation-triangle");
            modelAndView.addObject("notificationLevel", "danger");
            modelAndView.setViewName("topic_create");
            modelAndView.addObject("topic", topic);
            return modelAndView;
        }

        topic = topicService.save(topic);
        // Setting message
        redirectAttributes.addFlashAttribute("notificationMessage", "TopicController.topicCreated");
        redirectAttributes.addFlashAttribute("notificationIcon", "fa-check-circle");
        redirectAttributes.addFlashAttribute("notificationLevel", "success");
        modelAndView.setViewName("redirect:" + basePath + "/topic");
        return modelAndView;
    }

    @RequestMapping(value = "/delete/{topicId:[0-9]+}")
    public String delete(@PathVariable Integer topicId, RedirectAttributes redirectAttributes,
            HttpServletRequest request)
            throws InvalidTopicException {
        String basePath = MarbleUtil.getBasePath(request);
        topicService.delete(topicId);
        // Setting message
        redirectAttributes.addFlashAttribute("notificationMessage", "TopicController.topicDeleted");
        redirectAttributes.addFlashAttribute("notificationIcon", "fa-check-circle");
        redirectAttributes.addFlashAttribute("notificationLevel", "success");
        return "redirect:" + basePath + "/topic";
    }

    @RequestMapping(value = "/{topicId:[0-9]+}/execution", method = RequestMethod.GET)
    public ModelAndView execution(@PathVariable Integer topicId) throws InvalidTopicException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("forward:/execution/topic/" + topicId);
        return modelAndView;
    }

    @RequestMapping(value = "/{topicId:[0-9]+}/execution/extract", method = RequestMethod.GET)
    public ModelAndView executeExtractor(@PathVariable Integer topicId) throws InvalidTopicException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("forward:/execution/topic/" + topicId + "/extract");
        return modelAndView;
    }

    @RequestMapping(value = "/{topicId:[0-9]+}/execution/process", method = RequestMethod.GET)
    public ModelAndView executeProcessor(@PathVariable Integer topicId) throws InvalidTopicException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("forward:/execution/topic/" + topicId + "/process");
        return modelAndView;
    }

    @RequestMapping(value = "/{topicId:[0-9]+}/execution/plot", method = RequestMethod.GET)
    public ModelAndView executePlotter(@PathVariable Integer topicId) throws InvalidTopicException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("forward:/execution/topic/" + topicId + "/plot");
        return modelAndView;
    }

    @RequestMapping(value = "/{topicId:[0-9]+}/plot", method = RequestMethod.GET)
    public ModelAndView plot(@PathVariable Integer topicId) throws InvalidTopicException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("forward:/plot/topic/" + topicId);
        return modelAndView;
    }

    /*
     * @RequestMapping(value = "/{topicId:[0-9]+}/plot/{plotId:[0-9]+}", method
     * = RequestMethod.GET)
     * public ModelAndView plot(@PathVariable Integer topicId, @PathVariable
     * Integer plotId) throws InvalidTopicException {
     * ModelAndView modelAndView = new ModelAndView();
     * modelAndView.setViewName("forward:/plot/" + plotId);
     * return modelAndView;
     * }
     */

    @RequestMapping(value = "/{topicId:[0-9]+}/plot/create", method = RequestMethod.GET)
    public ModelAndView createPlot(@PathVariable Integer topicId) throws InvalidTopicException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("forward:/plot/topic/" + topicId + "/create");
        return modelAndView;
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