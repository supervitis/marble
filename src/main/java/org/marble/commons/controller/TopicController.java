package org.marble.commons.controller;

import javax.validation.Valid;

import org.marble.commons.dao.TopicDao;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.dao.model.TopicStatus;
import org.marble.commons.exception.InvalidTopicException;
import org.marble.commons.exception.InvalidUserException;
import org.marble.commons.model.SignupForm;
import org.marble.commons.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/topic")
public class TopicController {

	@Autowired
	TopicService topicService;

	@RequestMapping
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView("topics");
		return modelAndView;
	}

	@RequestMapping(value = "/edit/{topicId}", method = RequestMethod.GET)
	public ModelAndView edit(@PathVariable Integer topicId) {
		ModelAndView modelAndView = new ModelAndView();

		Topic topic;
		try {
			topic = topicService.getTopic(topicId);
		} catch (InvalidTopicException e) {
			// TODO Do something
			modelAndView.setViewName("error");
			return modelAndView;
		}
		modelAndView.setViewName("edit_topic");
		modelAndView.addObject("topic", topic);
		return modelAndView;
	}

	@RequestMapping(value = "/edit/{topicId}", method = RequestMethod.POST)
	public ModelAndView save(@PathVariable Integer topicId, @Valid Topic topic, BindingResult result) {
		ModelAndView modelAndView = new ModelAndView();

		try {
			topicService.updateTopic(topic);
		} catch (InvalidTopicException e) {
			// TODO Do something
			modelAndView.setViewName("error");
			return modelAndView;
		}

		modelAndView.setViewName("edit_topic");
		modelAndView.addObject("topic", topic);
		
		//TODO Set notification message and list view
		
		return modelAndView;
	}
}