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
import org.marble.commons.dao.model.StreamingStatus;
import org.marble.commons.dao.model.StreamingTopic;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.exception.InvalidDatasetException;
import org.marble.commons.exception.InvalidStreamingTopicException;
import org.marble.commons.exception.InvalidTopicException;
import org.marble.commons.model.ExecutionModuleParameters;
import org.marble.commons.model.StreamingTopicInfo;
import org.marble.commons.service.DatasetService;
import org.marble.commons.service.DatasetServiceImpl;
import org.marble.commons.service.StreamingTopicService;
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
import com.mongodb.MongoClient;

import twitter4j.GeoLocation;
import twitter4j.JSONObject;
import twitter4j.Query.Unit;

@Controller
@RequestMapping("/streaming_topic")
public class StreamingTopicController {

    // private static final Logger log =
    // LoggerFactory.getLogger(PlotController.class);
	@Autowired
	DatasetService datasetService;

    @Autowired
    StreamingTopicService streaming_topicService;

    @Autowired
    private Validator validator;

    @RequestMapping
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("streaming_topics_list");
        modelAndView.addObject("streaming_topics", streaming_topicService.findAll());
        return modelAndView;
    }

    @RequestMapping(value = "/{streaming_topicId:[0-9]+}", method = RequestMethod.GET)
    public ModelAndView info(@PathVariable Integer streaming_topicId) throws InvalidStreamingTopicException {
        ModelAndView modelAndView = new ModelAndView();
        StreamingTopic streamingTopic;
        streamingTopic = streaming_topicService.findOne(streaming_topicId);
        StreamingTopicInfo streaming_topicInfo;
        streaming_topicInfo = streaming_topicService.info(streaming_topicId);
        modelAndView.setViewName("streaming_topic_info");
        modelAndView.addObject("streaming_topicInfo", streaming_topicInfo);
        modelAndView.addObject("streaming_topic", streamingTopic);
        return modelAndView;
    }
    
    @RequestMapping(value = "/{streamingTopicId:[0-9]+}/download", method = RequestMethod.GET)
    public ModelAndView downloadTopic(@PathVariable Integer streamingTopicId, HttpServletRequest request, HttpServletResponse response) throws InvalidTopicException, InvalidStreamingTopicException {

        String basePath = MarbleUtil.getBasePath(request);
        ModelAndView modelAndView = new ModelAndView();
        try {
        	StreamingTopic streamingTopic;
            streamingTopic = streaming_topicService.findOne(streamingTopicId);
			response.setHeader("Content-Disposition",
                    "attachment;filename=" + streamingTopic.getName() + ".json");
			
			
			PrintWriter out = response.getWriter();
			List<StreamingStatus> statuses = streaming_topicService.findAllStatusByStreamingTopicId(streamingTopic.getId());
			for(StreamingStatus originalStatus : statuses){
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
    
    @RequestMapping(value = "/{streaming_topicId:[0-9]+}/edit", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable Integer streaming_topicId) throws InvalidStreamingTopicException {
        ModelAndView modelAndView = new ModelAndView();

        StreamingTopic streaming_topic;
        streaming_topic = streaming_topicService.findOne(streaming_topicId);
        modelAndView.setViewName("streaming_topic_edit");
        modelAndView.addObject("streaming_topic", streaming_topic);

    	Map<String,String> geoUnits= new LinkedHashMap<String,String>();
    	geoUnits.put("km", "Kilometers");
    	geoUnits.put("mi", "Miles");
    	modelAndView.addObject("geoUnits", geoUnits);
        return modelAndView;
    }

    @RequestMapping(value = "/{streaming_topicId:[0-9]+}/edit", method = RequestMethod.POST)
    public ModelAndView save(@Valid StreamingTopic streaming_topic, BindingResult result, @PathVariable Integer streaming_topicId,
            RedirectAttributes redirectAttributes, HttpServletRequest request)
            throws InvalidStreamingTopicException, IllegalStateException, InvalidDatasetException, IOException {

        String basePath = MarbleUtil.getBasePath(request);
        ModelAndView modelAndView = new ModelAndView();

        if (result.hasErrors()) {
            modelAndView.addObject("notificationMessage", "StreamingTopicController.editStreamingTopicError");
            modelAndView.addObject("notificationIcon", "fa-exclamation-triangle");
            modelAndView.addObject("notificationLevel", "danger");
            modelAndView.setViewName("streaming_topic_edit");
            modelAndView.addObject("streaming_topic", streaming_topic);
            return modelAndView;
        }
        
        streaming_topic = streaming_topicService.save(streaming_topic);
        
        redirectAttributes.addFlashAttribute("notificationMessage", "StreamingTopicController.streaming_topicModified");
        redirectAttributes.addFlashAttribute("notificationIcon", "fa-check-circle");
        redirectAttributes.addFlashAttribute("notificationLevel", "success");
        modelAndView.setViewName("redirect:" + basePath + "/streaming_topic/"+ streaming_topic.getId());
        return modelAndView;
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create() throws InvalidStreamingTopicException {
        ModelAndView modelAndView = new ModelAndView();

        StreamingTopic streaming_topic = new StreamingTopic();
        modelAndView.setViewName("streaming_topic_create");
        modelAndView.addObject("streaming_topic", streaming_topic);
    	Map<String,String> geoUnits= new LinkedHashMap<String,String>();
    	geoUnits.put("km", "Kilometers");
    	geoUnits.put("mi", "Miles");
    	modelAndView.addObject("geoUnits", geoUnits);
        return modelAndView;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(@ModelAttribute("streaming_topic") @Valid StreamingTopic streaming_topic, BindingResult result,
            RedirectAttributes redirectAttributes, HttpServletRequest request)
            throws InvalidStreamingTopicException, IllegalStateException, InvalidDatasetException, IOException {

        String basePath = MarbleUtil.getBasePath(request);
        ModelAndView modelAndView = new ModelAndView();

        if (result.hasErrors()) {
            modelAndView.addObject("notificationMessage", "StreamingTopicController.addStreamingTopicError");
            modelAndView.addObject("notificationIcon", "fa-exclamation-triangle");
            modelAndView.addObject("notificationLevel", "danger");
            modelAndView.setViewName("streaming_topic_create");
            modelAndView.addObject("streaming_topic", streaming_topic);
            return modelAndView;
        }

        streaming_topic = streaming_topicService.save(streaming_topic);
        
        // Setting message
        redirectAttributes.addFlashAttribute("notificationMessage", "StreamingTopicController.streaming_topicCreated");
        redirectAttributes.addFlashAttribute("notificationIcon", "fa-check-circle");
        redirectAttributes.addFlashAttribute("notificationLevel", "success");
        modelAndView.setViewName("redirect:" + basePath + "/streaming_topic/" + streaming_topic.getId());
        return modelAndView;
    }

    @RequestMapping(value = "/{streaming_topicId:[0-9]+}/delete")
    public String delete(@PathVariable Integer streaming_topicId, RedirectAttributes redirectAttributes,
            HttpServletRequest request)
            throws InvalidStreamingTopicException {
        String basePath = MarbleUtil.getBasePath(request);
        streaming_topicService.delete(streaming_topicId);
        // Setting message
        redirectAttributes.addFlashAttribute("notificationMessage", "StreamingTopicController.streaming_topicDeleted");
        redirectAttributes.addFlashAttribute("notificationIcon", "fa-check-circle");
        redirectAttributes.addFlashAttribute("notificationLevel", "success");
        return "redirect:" + basePath + "/streaming_topic";
    }

    @RequestMapping(value = "/{streaming_topicId:[0-9]+}/execution", method = RequestMethod.GET)
    public ModelAndView execution(@PathVariable Integer streaming_topicId) throws InvalidStreamingTopicException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("forward:/execution/streaming_topic/" + streaming_topicId);
        return modelAndView;
    }

    @RequestMapping(value = "/{streaming_topicId:[0-9]+}/execution/extract", method = RequestMethod.GET)
    public ModelAndView executeExtractor(@PathVariable Integer streaming_topicId) throws InvalidStreamingTopicException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("forward:/execution/streaming_topic/" + streaming_topicId + "/extract");
        return modelAndView;
    }

    @RequestMapping(value = "/{streaming_topicId:[0-9]+}/execution/process", method = RequestMethod.GET)
    public ModelAndView executeProcessor(@PathVariable Integer streaming_topicId) throws InvalidStreamingTopicException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("forward:/execution/streaming_topic/" + streaming_topicId + "/process");
        return modelAndView;
    }

    @RequestMapping(value = "/{streaming_topicId:[0-9]+}/process/execute", method = RequestMethod.GET)
    public ModelAndView processExecuteRequest(@PathVariable Integer streaming_topicId) throws InvalidStreamingTopicException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("forward:/process/streaming_topic/" + streaming_topicId + "/execute");
        return modelAndView;
    }
    
    @RequestMapping(value = "/{streaming_topicId:[0-9]+}/process/execute", method = RequestMethod.POST)
    public ModelAndView processExecuteResponse(@PathVariable Integer streaming_topicId,
            ExecutionModuleParameters moduleParameters) throws InvalidStreamingTopicException {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("forward:/process/streaming_topic/" + streaming_topicId + "/execute");
        return modelAndView;
    }
    
    @RequestMapping(value = "/{streaming_topicId:[0-9]+}/execution/plot", method = RequestMethod.GET)
    public ModelAndView executePlotter(@PathVariable Integer streaming_topicId) throws InvalidStreamingTopicException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("forward:/execution/streaming_topic/" + streaming_topicId + "/plot");
        return modelAndView;
    }

    @RequestMapping(value = "/{streaming_topicId:[0-9]+}/plot", method = RequestMethod.GET)
    public ModelAndView plot(@PathVariable Integer streaming_topicId) throws InvalidStreamingTopicException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("forward:/plot/streaming_topic/" + streaming_topicId);
        return modelAndView;
    }

    @RequestMapping(value = "/{streaming_topicId:[0-9]+}/plot/create", method = RequestMethod.GET)
    public ModelAndView createPlot(@PathVariable Integer streaming_topicId) throws InvalidStreamingTopicException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("forward:/plot/streaming_topic/" + streaming_topicId + "/create");
        return modelAndView;
    }

    @RequestMapping(value = "/{streaming_topicId:[0-9]+}/plot/create", method = RequestMethod.POST)
    public ModelAndView createPlotResponse(@PathVariable Integer streaming_topicId,
            ExecutionModuleParameters moduleParameters) throws InvalidStreamingTopicException {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("forward:/plot/streaming_topic/" + streaming_topicId + "/create");
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