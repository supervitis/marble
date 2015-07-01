package org.marble.commons.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.marble.commons.dao.model.Dataset;
import org.marble.commons.exception.InvalidDatasetException;
import org.marble.commons.service.DatasetService;
import org.marble.commons.util.MarbleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/datasets")
public class DatasetController {

    @Autowired
    DatasetService datasetService;

    @RequestMapping
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("datasets_list");
        modelAndView.addObject("datasets", datasetService.getDatasets());
        return modelAndView;
    }

    @RequestMapping(value = "/edit/{datasetId}", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable Integer datasetId) throws InvalidDatasetException {
        ModelAndView modelAndView = new ModelAndView();

        Dataset dataset;
        dataset = datasetService.getDataset(datasetId);
        modelAndView.setViewName("dataset_edit");
        modelAndView.addObject("dataset", dataset);
        return modelAndView;
    }

    @RequestMapping(value = "/edit/{datasetId}", method = RequestMethod.POST)
    public ModelAndView save(@PathVariable Integer datasetId, @Valid Dataset dataset,
            BindingResult result,@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, HttpServletRequest request) throws InvalidDatasetException {

    	
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("dataset_edit");
        modelAndView.addObject("dataset", dataset);

        if (result.hasErrors()) {
            modelAndView.addObject("notificationMessage", "DatasetController.editDatasetError");
            modelAndView.addObject("notificationIcon", "fa-exclamation-triangle");
            modelAndView.addObject("notificationLevel", "danger");
            return modelAndView;
        }

        try {
			datasetService.updateDataset(dataset,file);
		} catch (IllegalStateException | IOException e) {
			modelAndView.addObject("notificationMessage", "DatasetController.editDatasetError");
            modelAndView.addObject("notificationIcon", "fa-exclamation-triangle");
            modelAndView.addObject("notificationLevel", "danger");
            return modelAndView;
		}

        modelAndView.addObject("notificationMessage", "DatasetController.datasetModified");
        modelAndView.addObject("notificationIcon", "fa-check-circle");
        modelAndView.addObject("notificationLevel", "success");

        // TODO Set list view as return

        return modelAndView;
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create() throws InvalidDatasetException {
        ModelAndView modelAndView = new ModelAndView();

        Dataset dataset = new Dataset();
        modelAndView.setViewName("create_dataset");
        modelAndView.addObject("dataset", dataset);
        return modelAndView;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(@Valid Dataset dataset, BindingResult result,
            RedirectAttributes redirectAttributes, HttpServletRequest request,
            @RequestParam("file") MultipartFile file) throws InvalidDatasetException {

        String basePath = MarbleUtil.getBasePath(request);
        ModelAndView modelAndView = new ModelAndView();

        if (result.hasErrors()) {
            modelAndView.addObject("notificationMessage", "DatasetController.addDatasetError");
            modelAndView.addObject("notificationIcon", "fa-exclamation-triangle");
            modelAndView.addObject("notificationLevel", "danger");
            modelAndView.setViewName("create_dataset");
            modelAndView.addObject("dataset", dataset);
            return modelAndView;
        }
        try{
        dataset = datasetService.createDataset(dataset, file);
	    } catch (IllegalStateException | IOException e) {
			modelAndView.addObject("notificationMessage", "DatasetController.editDatasetError");
	        modelAndView.addObject("notificationIcon", "fa-exclamation-triangle");
	        modelAndView.addObject("notificationLevel", "danger");
	        return modelAndView;
		}
        // Setting message
        redirectAttributes.addFlashAttribute("notificationMessage", "DatasetController.datasetCreated");
        redirectAttributes.addFlashAttribute("notificationIcon", "fa-check-circle");
        redirectAttributes.addFlashAttribute("notificationLevel", "success");
        modelAndView.setViewName("redirect:" + basePath + "/datasets");
        return modelAndView;
    }

    @RequestMapping(value = "/delete/{datasetId}")
    public String delete(@PathVariable Integer datasetId, RedirectAttributes redirectAttributes,
            HttpServletRequest request) throws InvalidDatasetException {
        String basePath = MarbleUtil.getBasePath(request);
        datasetService.deleteDataset(datasetId);
        // Setting message
        redirectAttributes.addFlashAttribute("notificationMessage", "DatasetController.datasetDeleted");
        redirectAttributes.addFlashAttribute("notificationIcon", "fa-check-circle");
        redirectAttributes.addFlashAttribute("notificationLevel", "success");
        return "redirect:" + basePath + "/datasets";
    }

}