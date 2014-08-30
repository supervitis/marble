package org.marble.commons.controller.rest;

import org.marble.commons.dao.model.Execution;
import org.marble.commons.exception.InvalidExecutionException;
import org.marble.commons.model.Command;
import org.marble.commons.service.ExecutionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest/execution")
public class ExecutionRestController {

    private static final Logger log = LoggerFactory.getLogger(ExecutionRestController.class);
    
    @Autowired
    private ExecutionService executionService;
    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    Execution get(@PathVariable("id") Integer id) throws InvalidExecutionException {
        Execution execution = executionService.getExecution(id);
        return execution;
    }
    
    @RequestMapping(value = "/{id}/command",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    Execution command(@PathVariable("id") Integer id, @RequestBody Command command) throws InvalidExecutionException {
        Execution execution = executionService.getExecution(id);
        execution.setCommand(command.getCommand());
        execution = executionService.updateExecution(execution);
        return execution;
    }

}
