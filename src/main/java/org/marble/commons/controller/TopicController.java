package org.marble.commons.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.marble.commons.dao.model.Dataset;
import org.marble.commons.dao.model.OriginalStatus;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.exception.InvalidDatasetException;
import org.marble.commons.exception.InvalidTopicException;
import org.marble.commons.model.ExecutionModuleParameters;
import org.marble.commons.model.TopicInfo;
import org.marble.commons.service.DatasetService;
import org.marble.commons.service.DatasetServiceImpl;
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

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import twitter4j.GeoLocation;
import twitter4j.JSONObject;
import twitter4j.Query.Unit;

@Controller
@RequestMapping("/topic")
public class TopicController {

    // private static final Logger log =
    // LoggerFactory.getLogger(PlotController.class);
	@Autowired
	DatasetService datasetService;

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

    @RequestMapping(value = "/{topicId:[0-9]+}", method = RequestMethod.GET)
    public ModelAndView info(@PathVariable Integer topicId) throws InvalidTopicException {
        ModelAndView modelAndView = new ModelAndView();

        TopicInfo topicInfo;
        topicInfo = topicService.info(topicId);
        modelAndView.setViewName("topic_info");
        modelAndView.addObject("topicInfo", topicInfo);
        return modelAndView;
    }
    
    @RequestMapping(value = "/{topicId:[0-9]+}/download", method = RequestMethod.GET)
    public ModelAndView downloadTopic(@PathVariable Integer topicId, HttpServletRequest request, HttpServletResponse response) throws InvalidTopicException {

        String basePath = MarbleUtil.getBasePath(request);
        ModelAndView modelAndView = new ModelAndView();
        try {
        	Topic topic;
            topic = topicService.findOne(topicId);
			response.setHeader("Content-Disposition",
                    "attachment;filename=" + topic.getName() + ".json");
			
			
			PrintWriter out = response.getWriter();
			List<OriginalStatus> statuses = topicService.findAllStatusByTopicId(topic.getId());
			for(OriginalStatus originalStatus : statuses){
				JSONObject jsonObject = new JSONObject(originalStatus);
				out.println(jsonObject.toString());
			}
			out.close();
		} catch (IOException ex) {
			throw new RuntimeException("IOError writing file to output stream");
		}
        modelAndView.setViewName("redirect:" + basePath + "/datasets");
        return modelAndView;
    }
    
    @RequestMapping(value = "/{topicId:[0-9]+}/edit", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable Integer topicId) throws InvalidTopicException {
        ModelAndView modelAndView = new ModelAndView();

        Topic topic;
        topic = topicService.findOne(topicId);
        modelAndView.setViewName("topic_edit");
        modelAndView.addObject("topic", topic);

    	Map<String,String> geoUnits= new LinkedHashMap<String,String>();
    	geoUnits.put("km", "Kilometers");
    	geoUnits.put("mi", "Miles");
    	modelAndView.addObject("geoUnits", geoUnits);
        return modelAndView;
    }

    @RequestMapping(value = "/{topicId:[0-9]+}/edit", method = RequestMethod.POST)
    public ModelAndView save(@Valid Topic topic, BindingResult result, @PathVariable Integer topicId,
            RedirectAttributes redirectAttributes, HttpServletRequest request)
            throws InvalidTopicException, IllegalStateException, InvalidDatasetException, IOException {

        String basePath = MarbleUtil.getBasePath(request);
        ModelAndView modelAndView = new ModelAndView();

        if (result.hasErrors()) {
            modelAndView.addObject("notificationMessage", "TopicController.editTopicError");
            modelAndView.addObject("notificationIcon", "fa-exclamation-triangle");
            modelAndView.addObject("notificationLevel", "danger");
            modelAndView.setViewName("topic_edit");
            modelAndView.addObject("topic", topic);
            Map<String,String> geoUnits= new LinkedHashMap<String,String>();
        	geoUnits.put("km", "Kilometers");
        	geoUnits.put("mi", "Miles");
        	modelAndView.addObject("geoUnits", geoUnits);
            return modelAndView;
        }
        
        topic = topicService.save(topic);
       
        redirectAttributes.addFlashAttribute("notificationMessage", "TopicController.topicModified");
        redirectAttributes.addFlashAttribute("notificationIcon", "fa-check-circle");
        redirectAttributes.addFlashAttribute("notificationLevel", "success");
        modelAndView.setViewName("redirect:" + basePath + "/topic/"+ topic.getId());
        return modelAndView;
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create() throws InvalidTopicException {
        ModelAndView modelAndView = new ModelAndView();

        Topic topic = new Topic();
        modelAndView.setViewName("topic_create");
        modelAndView.addObject("topic", topic);
    	Map<String,String> geoUnits= new LinkedHashMap<String,String>();
    	geoUnits.put("km", "Kilometers");
    	geoUnits.put("mi", "Miles");
    	modelAndView.addObject("geoUnits", geoUnits);
        return modelAndView;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(@ModelAttribute("topic") @Valid Topic topic, BindingResult result,
            RedirectAttributes redirectAttributes, HttpServletRequest request)
            throws InvalidTopicException, IllegalStateException, InvalidDatasetException, IOException {

        String basePath = MarbleUtil.getBasePath(request);
        ModelAndView modelAndView = new ModelAndView();

        if (result.hasErrors()) {
            modelAndView.addObject("notificationMessage", "TopicController.addTopicError");
            modelAndView.addObject("notificationIcon", "fa-exclamation-triangle");
            modelAndView.addObject("notificationLevel", "danger");
            modelAndView.setViewName("topic_create");
            modelAndView.addObject("topic", topic);
            Map<String,String> geoUnits= new LinkedHashMap<String,String>();
        	geoUnits.put("km", "Kilometers");
        	geoUnits.put("mi", "Miles");
        	modelAndView.addObject("geoUnits", geoUnits);
            return modelAndView;
        }

        topic = topicService.save(topic);
        // Setting message
        redirectAttributes.addFlashAttribute("notificationMessage", "TopicController.topicCreated");
        redirectAttributes.addFlashAttribute("notificationIcon", "fa-check-circle");
        redirectAttributes.addFlashAttribute("notificationLevel", "success");
        modelAndView.setViewName("redirect:" + basePath + "/topic/" + topic.getId());
        return modelAndView;
    }

    @RequestMapping(value = "/{topicId:[0-9]+}/delete")
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

    @RequestMapping(value = "/{topicId:[0-9]+}/process/execute", method = RequestMethod.GET)
    public ModelAndView processExecuteRequest(@PathVariable Integer topicId) throws InvalidTopicException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("forward:/process/topic/" + topicId + "/execute");
        return modelAndView;
    }
    
    @RequestMapping(value = "/{topicId:[0-9]+}/process/execute", method = RequestMethod.POST)
    public ModelAndView processExecuteResponse(@PathVariable Integer topicId,
            ExecutionModuleParameters moduleParameters) throws InvalidTopicException {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("forward:/process/topic/" + topicId + "/execute");
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

    @RequestMapping(value = "/{topicId:[0-9]+}/plot/create", method = RequestMethod.GET)
    public ModelAndView createPlot(@PathVariable Integer topicId) throws InvalidTopicException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("forward:/plot/topic/" + topicId + "/create");
        return modelAndView;
    }

    @RequestMapping(value = "/{topicId:[0-9]+}/plot/create", method = RequestMethod.POST)
    public ModelAndView createPlotResponse(@PathVariable Integer topicId,
            ExecutionModuleParameters moduleParameters) throws InvalidTopicException {
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