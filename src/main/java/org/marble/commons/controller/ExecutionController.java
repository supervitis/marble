package org.marble.commons.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;

import org.marble.commons.dao.model.Execution;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.exception.InvalidExecutionException;
import org.marble.commons.exception.InvalidTopicException;
import org.marble.commons.service.ExecutionService;
import org.marble.commons.service.TopicService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/execution")
public class ExecutionController {

	@Autowired
	ExecutionService executionService;

	/*@RequestMapping
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView("topics_list");
		modelAndView.addObject("topics", topicService.getTopics());
		return modelAndView;
	}*/

	@RequestMapping(value = "/{executionId}", method = RequestMethod.GET)
	public ModelAndView edit(@PathVariable Integer executionId) throws InvalidExecutionException {
		ModelAndView modelAndView = new ModelAndView();

		Execution execution;
		execution = executionService.getExecution(executionId);
		modelAndView.setViewName("view_execution");
		modelAndView.addObject("execution", execution);
		return modelAndView;
	}
	
	/*@RequestMapping(value = "/edit/{topicId}", method = RequestMethod.POST)
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
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() throws InvalidTopicException {
		ModelAndView modelAndView = new ModelAndView();

		Topic topic = new Topic();
		modelAndView.setViewName("create_topic");
		modelAndView.addObject("topic", topic);
		return modelAndView;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView create(@Valid Topic topic, BindingResult result, RedirectAttributes redirectAttributes) throws InvalidTopicException {

		ModelAndView modelAndView = new ModelAndView();
		
		if (result.hasErrors()) {
			modelAndView.addObject("notificationMessage", "TopicController.addTopicError");
			modelAndView.addObject("notificationIcon", "fa-exclamation-triangle");
			modelAndView.addObject("notificationLevel", "danger");
			modelAndView.setViewName("create_topic");
			modelAndView.addObject("topic", topic);
			return modelAndView;
		}

		topic = topicService.createTopic(topic);
		// Setting message
		redirectAttributes.addFlashAttribute("notificationMessage", "TopicController.topicCreated");
		redirectAttributes.addFlashAttribute("notificationIcon", "fa-check-circle");
		redirectAttributes.addFlashAttribute("notificationLevel", "success");
		modelAndView.setViewName("redirect:/topic");
		return modelAndView;
	}

	@RequestMapping(value = "/delete/{topicId}")
	public String delete(@PathVariable Integer topicId, RedirectAttributes redirectAttributes) throws InvalidTopicException {
		topicService.deleteTopic(topicId);
		// Setting message
		redirectAttributes.addFlashAttribute("notificationMessage", "TopicController.topicDeleted");
		redirectAttributes.addFlashAttribute("notificationIcon", "fa-check-circle");
		redirectAttributes.addFlashAttribute("notificationLevel", "success");
		return "redirect:/topic";
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		sdf.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
		// You can register other Custom Editors with the WebDataBinder, like CustomNumberEditor for Integers and Longs,
		// or StringTrimmerEditor for Strings
	}*/
}