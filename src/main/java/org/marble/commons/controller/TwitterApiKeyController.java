package org.marble.commons.controller;

import javax.validation.Valid;

import org.marble.commons.dao.model.TwitterApiKey;
import org.marble.commons.exception.InvalidTwitterApiKeyException;
import org.marble.commons.service.TwitterApiKeyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/keys/twitter")
public class TwitterApiKeyController {

	@Autowired
	TwitterApiKeyService twitterApiKeyService;

	@RequestMapping
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView("twitter_api_keys_list");
		modelAndView.addObject("twitter_api_keys", twitterApiKeyService.getTwitterApiKeys());
		return modelAndView;
	}

	@RequestMapping(value = "/edit/{twitterApiKeyId}", method = RequestMethod.GET)
	public ModelAndView edit(@PathVariable Integer twitterApiKeyId) throws InvalidTwitterApiKeyException {
		ModelAndView modelAndView = new ModelAndView();

		TwitterApiKey twitterApiKey;
		twitterApiKey = twitterApiKeyService.getTwitterApiKey(twitterApiKeyId);
		modelAndView.setViewName("edit_twitter_api_key");
		modelAndView.addObject("twitter_api_key", twitterApiKey);
		return modelAndView;
	}
	
	@RequestMapping(value = "/edit/{twitterApiKeyId}", method = RequestMethod.POST)
	public ModelAndView save(@PathVariable Integer twitterApiKeyId, @Valid TwitterApiKey twitterApiKey, BindingResult result) throws InvalidTwitterApiKeyException {

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("edit_twitter_api_key");
		modelAndView.addObject("twitter_api_key", twitterApiKey);

		if (result.hasErrors()) {
			modelAndView.addObject("notificationMessage", "TwitterApiKeyController.editTwitterApiKeyError");
			modelAndView.addObject("notificationIcon", "fa-exclamation-triangle");
			modelAndView.addObject("notificationLevel", "danger");
			return modelAndView;
		}

		twitterApiKeyService.updateTwitterApiKey(twitterApiKey);

		modelAndView.addObject("notificationMessage", "TwitterApiKeyController.twitterApiKeyModified");
		modelAndView.addObject("notificationIcon", "fa-check-circle");
		modelAndView.addObject("notificationLevel", "success");

		// TODO Set list view as return

		return modelAndView;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() throws InvalidTwitterApiKeyException {
		ModelAndView modelAndView = new ModelAndView();

		TwitterApiKey twitterApiKey = new TwitterApiKey();
		modelAndView.setViewName("create_twitter_api_key");
		modelAndView.addObject("twitter_api_key", twitterApiKey);
		return modelAndView;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView create(@Valid TwitterApiKey twitterApiKey, BindingResult result, RedirectAttributes redirectAttributes) throws InvalidTwitterApiKeyException {

		ModelAndView modelAndView = new ModelAndView();
		
		if (result.hasErrors()) {
			modelAndView.addObject("notificationMessage", "TwitterApiKeyController.addTwitterApiKeyError");
			modelAndView.addObject("notificationIcon", "fa-exclamation-triangle");
			modelAndView.addObject("notificationLevel", "danger");
			modelAndView.setViewName("create_twitter_api_key");
			modelAndView.addObject("twitter_api_key", twitterApiKey);
			return modelAndView;
		}

		twitterApiKey = twitterApiKeyService.createTwitterApiKey(twitterApiKey);
		// Setting message
		redirectAttributes.addFlashAttribute("notificationMessage", "TwitterApiKeyController.twitterApiKeyCreated");
		redirectAttributes.addFlashAttribute("notificationIcon", "fa-check-circle");
		redirectAttributes.addFlashAttribute("notificationLevel", "success");
		modelAndView.setViewName("redirect:/admin/keys/twitter");
		return modelAndView;
	}

	@RequestMapping(value = "/delete/{twitterApiKeyId}")
	public String delete(@PathVariable Integer twitterApiKeyId, RedirectAttributes redirectAttributes) throws InvalidTwitterApiKeyException {
		twitterApiKeyService.deleteTwitterApiKey(twitterApiKeyId);
		// Setting message
		redirectAttributes.addFlashAttribute("notificationMessage", "TwitterApiKeyController.twitterApiKeyDeleted");
		redirectAttributes.addFlashAttribute("notificationIcon", "fa-check-circle");
		redirectAttributes.addFlashAttribute("notificationLevel", "success");
		return "redirect:/admin/keys/twitter";
	}

}