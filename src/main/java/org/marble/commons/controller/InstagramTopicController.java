package org.marble.commons.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.marble.commons.dao.model.InstagramTopic;
import org.marble.commons.dao.model.StreamingTopic;
import org.marble.commons.exception.InvalidDatasetException;
import org.marble.commons.exception.InvalidInstagramTopicException;
import org.marble.commons.model.ExecutionModuleParameters;
import org.marble.commons.model.InstagramTopicInfo;
import org.marble.commons.service.DatasetService;
import org.marble.commons.service.InstagramTopicService;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mongodb.DBCursor;

@Controller
@RequestMapping("/instagram_topic")
public class InstagramTopicController {

    private static final Logger log = LoggerFactory.getLogger(InstagramTopic.class);
	@Autowired
	DatasetService datasetService;

    @Autowired
    InstagramTopicService instagram_topicService;

    @Autowired
    private Validator validator;

    @RequestMapping
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("instagram_topics_list");
        modelAndView.addObject("instagram_topics", instagram_topicService.findAll());
        return modelAndView;
    }

    @RequestMapping(value = "/{instagram_topicId:[0-9]+}", method = RequestMethod.GET)
    public ModelAndView info(@PathVariable Integer instagram_topicId) throws InvalidInstagramTopicException {
        ModelAndView modelAndView = new ModelAndView();

        InstagramTopicInfo instagram_topicInfo;
        instagram_topicInfo = instagram_topicService.info(instagram_topicId);
        modelAndView.setViewName("instagram_topic_info");
        modelAndView.addObject("instagram_topicInfo", instagram_topicInfo);
        return modelAndView;
    }
    
    
    @RequestMapping(value = "/{instagramTopicId:[0-9]+}/download", method = RequestMethod.GET)
    public ModelAndView downloadInstagramTopic(@PathVariable Integer instagramTopicId, HttpServletRequest request, HttpServletResponse response) throws InvalidInstagramTopicException {

        String basePath = MarbleUtil.getBasePath(request);
        ModelAndView modelAndView = new ModelAndView();
        try {
        	InstagramTopic instagramTopic;
            instagramTopic = instagram_topicService.findOne(instagramTopicId);
			response.setHeader("Content-Disposition",
                    "attachment;filename=" + instagramTopic.getName() + ".json");
			PrintWriter out = response.getWriter();
			log.error(instagramTopic.getName() + "con id " + instagramTopic.getId() + " - " + instagramTopicId);
			DBCursor cursor = instagram_topicService.findCursorByInstagramTopicId(instagramTopicId);
			while(cursor.hasNext()){
				out.println(cursor.next().toString());
			}
			out.close();
		} catch (IOException ex) {
			throw new RuntimeException("IOError writing file to output stream");
		}
        modelAndView.setViewName("redirect:" + basePath + "/datasets");
        return modelAndView;
    }
    
    @RequestMapping(value = "/{instagram_topicId:[0-9]+}/edit", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable Integer instagram_topicId) throws InvalidInstagramTopicException {
        ModelAndView modelAndView = new ModelAndView();

        InstagramTopic instagram_topic;
        instagram_topic = instagram_topicService.findOne(instagram_topicId);
        modelAndView.setViewName("instagram_topic_edit");
        modelAndView.addObject("instagram_topic", instagram_topic);

    	Map<String,String> geoUnits= new LinkedHashMap<String,String>();
    	geoUnits.put("km", "Kilometers");
    	geoUnits.put("mi", "Miles");
    	modelAndView.addObject("geoUnits", geoUnits);
        return modelAndView;
    }

    @RequestMapping(value = "/{instagram_topicId:[0-9]+}/edit", method = RequestMethod.POST)
    public ModelAndView save(@Valid InstagramTopic instagram_topic, BindingResult result, @PathVariable Integer instagram_topicId,
            RedirectAttributes redirectAttributes, HttpServletRequest request)
            throws InvalidInstagramTopicException, IllegalStateException, InvalidDatasetException, IOException {

        String basePath = MarbleUtil.getBasePath(request);
        ModelAndView modelAndView = new ModelAndView();

        if (result.hasErrors()) {
            modelAndView.addObject("notificationMessage", "InstagramTopicController.editInstagramTopicError");
            modelAndView.addObject("notificationIcon", "fa-exclamation-triangle");
            modelAndView.addObject("notificationLevel", "danger");
            modelAndView.setViewName("instagram_topic_edit");
            modelAndView.addObject("instagram_topic", instagram_topic);
            Map<String,String> geoUnits= new LinkedHashMap<String,String>();
        	geoUnits.put("km", "Kilometers");
        	geoUnits.put("mi", "Miles");
        	modelAndView.addObject("geoUnits", geoUnits);
            return modelAndView;
        }

        instagram_topic = instagram_topicService.save(instagram_topic);
        
        redirectAttributes.addFlashAttribute("notificationMessage", "InstagramTopicController.instagram_topicModified");
        redirectAttributes.addFlashAttribute("notificationIcon", "fa-check-circle");
        redirectAttributes.addFlashAttribute("notificationLevel", "success");
       	modelAndView.setViewName("redirect:" + basePath + "/instagram_topic/"+ instagram_topic.getId());
        return modelAndView;
    }

    @RequestMapping(value = "/instagram", method = RequestMethod.GET)
    public void instagramInstagramTopic(HttpServletRequest request, HttpServletResponse response) {
    	response.setContentType("text/html");
        String challenge = request.getParameter("hub.challenge");
        log.info(challenge);
        PrintWriter out;
		try {
			out = response.getWriter();
	        out.print(challenge);
	        out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
    }
    
    @RequestMapping(value = "/instagram", method = RequestMethod.POST)
    public void saveInstagramInstagramTopic(HttpServletRequest request, HttpServletResponse response) {
    	
    	log.info("new instagram state");
        BufferedReader reader;
		try {
			reader = request.getReader();
			String line;
			while ((line = reader.readLine()) != null)
			      log.info(line);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.setStatus(HttpServletResponse.SC_OK);
        
    }
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create() throws InvalidInstagramTopicException {
        ModelAndView modelAndView = new ModelAndView();

        InstagramTopic instagram_topic = new InstagramTopic();
        modelAndView.setViewName("instagram_topic_create");
        modelAndView.addObject("instagram_topic", instagram_topic);
    	Map<String,String> geoUnits= new LinkedHashMap<String,String>();
    	geoUnits.put("km", "Kilometers");
    	geoUnits.put("mi", "Miles");
    	modelAndView.addObject("geoUnits", geoUnits);
        return modelAndView;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(@ModelAttribute("instagram_topic") @Valid InstagramTopic instagram_topic, BindingResult result,
            RedirectAttributes redirectAttributes, HttpServletRequest request)
            throws InvalidInstagramTopicException, IllegalStateException, InvalidDatasetException, IOException {

        String basePath = MarbleUtil.getBasePath(request);
        ModelAndView modelAndView = new ModelAndView();

        if (result.hasErrors()) {
            modelAndView.addObject("notificationMessage", "InstagramTopicController.addInstagramTopicError");
            modelAndView.addObject("notificationIcon", "fa-exclamation-triangle");
            modelAndView.addObject("notificationLevel", "danger");
            modelAndView.setViewName("instagram_topic_create");
            modelAndView.addObject("instagram_topic", instagram_topic);
            Map<String,String> geoUnits= new LinkedHashMap<String,String>();
        	geoUnits.put("km", "Kilometers");
        	geoUnits.put("mi", "Miles");
        	modelAndView.addObject("geoUnits", geoUnits);
            return modelAndView;
        }

        instagram_topic = instagram_topicService.save(instagram_topic);
        
        // Setting message
        redirectAttributes.addFlashAttribute("notificationMessage", "InstagramTopicController.instagram_topicCreated");
        redirectAttributes.addFlashAttribute("notificationIcon", "fa-check-circle");
        redirectAttributes.addFlashAttribute("notificationLevel", "success");
        modelAndView.setViewName("redirect:" + basePath + "/instagram_topic/" + instagram_topic.getId());
        return modelAndView;
    }

    @RequestMapping(value = "/{instagram_topicId:[0-9]+}/delete")
    public String delete(@PathVariable Integer instagram_topicId, RedirectAttributes redirectAttributes,
            HttpServletRequest request)
            throws InvalidInstagramTopicException {
        String basePath = MarbleUtil.getBasePath(request);
        instagram_topicService.delete(instagram_topicId);
        // Setting message
        redirectAttributes.addFlashAttribute("notificationMessage", "InstagramTopicController.instagram_topicDeleted");
        redirectAttributes.addFlashAttribute("notificationIcon", "fa-check-circle");
        redirectAttributes.addFlashAttribute("notificationLevel", "success");
        return "redirect:" + basePath + "/instagram_topic";
    }

    @RequestMapping(value = "/{instagram_topicId:[0-9]+}/execution", method = RequestMethod.GET)
    public ModelAndView execution(@PathVariable Integer instagram_topicId) throws InvalidInstagramTopicException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("forward:/execution/instagram_topic/" + instagram_topicId);
        return modelAndView;
    }

    @RequestMapping(value = "/{instagram_topicId:[0-9]+}/execution/extract", method = RequestMethod.GET)
    public ModelAndView executeExtractor(@PathVariable Integer instagram_topicId) throws InvalidInstagramTopicException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("forward:/execution/instagram_topic/" + instagram_topicId + "/extract");
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