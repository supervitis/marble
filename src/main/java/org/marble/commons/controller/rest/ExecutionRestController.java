package org.marble.commons.controller.rest;

import org.marble.commons.dao.model.Execution;
import org.marble.commons.exception.InvalidExecutionException;
import org.marble.commons.exception.InvalidTwitterApiKeyException;
import org.marble.commons.service.ExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest/execution")
public class ExecutionRestController {

    @Autowired
    private ExecutionService executionService;

    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    Execution get(@PathVariable("id") Integer id) throws InvalidExecutionException, InvalidTwitterApiKeyException {
        Execution execution = executionService.getExecution(id);
        return execution;
    }
}
