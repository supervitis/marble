package org.marble.commons.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;

import org.marble.commons.dao.TopicDao;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.dao.model.TopicStatus;
import org.marble.commons.exception.InvalidTopicException;
import org.marble.commons.exception.InvalidUserException;
import org.marble.commons.model.SignupForm;
import org.marble.commons.service.ResetServiceImpl;
import org.marble.commons.service.TopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/topic")
public class TopicController {

	private static final Logger log = LoggerFactory.getLogger(ResetServiceImpl.class);
	@Autowired
	TopicService topicService;

	@RequestMapping
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView("topics_list");
		modelAndView.addObject("topics", topicService.getTopics());
		return modelAndView;
	}

	@RequestMapping(value = "/edit/{topicId}", method = RequestMethod.GET)
	public ModelAndView edit(@PathVariable Integer topicId) throws InvalidTopicException {
		ModelAndView modelAndView = new ModelAndView();

		Topic topic;
		topic = topicService.getTopic(topicId);
		modelAndView.setViewName("edit_topic");
		modelAndView.addObject("topic", topic);
		return modelAndView;
	}

	@RequestMapping(value = "/edit/{topicId}", method = RequestMethod.POST)
	public ModelAndView save(@PathVariable Integer topicId, @Valid Topic topic, BindingResult result) throws InvalidTopicException {

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("edit_topic");
		modelAndView.addObject("topic", topic);

		if (result.hasErrors()) {
			modelAndView.addObject("notificationMessage", "TopicController.editTopicError");
			modelAndView.addObject("notificationIcon", "fa-exclamation-triangle");
			modelAndView.addObject("notificationLevel", "danger");
			return modelAndView;
		}

		topicService.updateTopic(topic);

		modelAndView.addObject("notificationMessage", "TopicController.topicModified");
		modelAndView.addObject("notificationIcon", "fa-check-circle");
		modelAndView.addObject("notificationLevel", "success");

		// TODO Set list view as return

		return modelAndView;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		sdf.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
		// You can register other Custom Editors with the WebDataBinder, like CustomNumberEditor for Integers and Longs,
		// or StringTrimmerEditor for Strings
	}
}