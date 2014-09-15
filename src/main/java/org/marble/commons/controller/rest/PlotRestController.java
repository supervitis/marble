package org.marble.commons.controller.rest;

import org.marble.commons.dao.model.Plot;
import org.marble.commons.exception.InvalidPlotException;
import org.marble.commons.service.PlotService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest/plot")
public class PlotRestController {

    // private static final Logger log =
    // LoggerFactory.getLogger(PlotRestController.class);

    @Autowired
    private PlotService plotService;

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    Plot get(@PathVariable("id") Integer id) throws InvalidPlotException {
        Plot plot = plotService.findOne(id);
        return plot;
    }

}
