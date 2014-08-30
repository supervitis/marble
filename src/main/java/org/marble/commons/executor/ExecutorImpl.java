package org.marble.commons.executor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.marble.commons.dao.model.Execution;
import org.marble.commons.exception.InvalidExecutionException;
import org.marble.commons.executor.extractor.ExtractorExecutor;
import org.marble.commons.model.ExecutionStatus;
import org.marble.commons.service.ExecutionService;
import org.marble.commons.util.MarbleUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

@Component
@Scope("prototype")
public class ExecutorImpl implements ExtractorExecutor {

    private static final Logger log = LoggerFactory.getLogger(ExecutorImpl.class);
    
    private static final String name = "ExecutorImpl";

    @Autowired
    ExecutionService executionService;

    @Autowired
    MongoOperations mongoOperation;

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    Execution execution;

    // Integer id;

    public ExecutorImpl() {

    }

    @Override
    public void setExecution(Execution execution) {
        this.execution = execution;
    }

    @Override
    public void run() {
        try {
            log.info("On your marks...");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        try {
        Integer id = execution.getId();
        log.info("Executor starting execution <" + id + ">, of type <" + execution.getType() + ">");

        // Changing execution state
        execution.setStatus(ExecutionStatus.Running);

        execution.appendLog(MarbleUtil.getDatedMessage("Will create something in the mongo.\n"));
        DBCollection collection = mongoOperation.getCollection("prueba");
        DBObject dbObject = (DBObject) JSON.parse("{process: " + id + "}");
        collection.insert(dbObject);

        for (int i = 0; i < 10; i++) {

            execution.appendLog(MarbleUtil.getDatedMessage("process is running.\n"));
            execution = executionService.updateExecution(execution);

            // Check for commands
            log.error("Current command: <"+execution.getCommand()+">");
            if (execution.getCommand() != null){
                execution.setStatus(ExecutionStatus.Stopped);
                execution.appendLog(MarbleUtil.getDatedMessage("Execution was stopped by the user."));
                execution = executionService.updateExecution(execution);

                return;
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                log.info("The execution was aborted.", e);
                execution.setStatus(ExecutionStatus.Aborted);
                execution = executionService.updateExecution(execution);
                return;
            }
        }
        
        execution.setStatus(ExecutionStatus.Stopped);
        execution.appendLog(MarbleUtil.getDatedMessage("Execution finished completely."));
        
            execution = executionService.updateExecution(execution);
        } catch (InvalidExecutionException e) {
            log.error("A fatal error ocurred while manipulating the Execution object.", e);
        }
    }

    @Override
    public String getName() {
        return this.name;
    }
}
