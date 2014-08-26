package org.marble.commons.thread;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.marble.commons.dao.model.Execution;
import org.marble.commons.dao.model.ExecutionStatus;
import org.marble.commons.dao.model.Topic;
import org.marble.commons.exception.InvalidExecutionException;
import org.marble.commons.service.ExecutionService;
import org.marble.commons.service.ResetServiceImpl;
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
public class ExecutorImpl implements Executor {

    private static final Logger log = LoggerFactory.getLogger(ExecutorImpl.class);

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

        Integer id = execution.getId();
        log.info("Executor starting execution <" + id + ">, of type <" + execution.getType() + ">");

        // Changing execution state
        execution.setStatus(ExecutionStatus.RUNNING);

        execution.appendLog(MarbleUtil.getDatedMessage("Will create something in the mongo.\n"));
        DBCollection collection = mongoOperation.getCollection("prueba");
        DBObject dbObject = (DBObject) JSON.parse("{process: " + id + "}");
        collection.insert(dbObject);

        for (int i = 0; i < 20; i++) {

            String date = dateFormat.format(new Date());

            log.info("MFC: " + date + ": process <" + id + "> is running.");
            execution.appendLog(MarbleUtil.getDatedMessage("process is running.\n"));
            try {
                execution = executionService.updateExecution(execution);
            } catch (InvalidExecutionException e) {
                log.info("MFC Error 2: ", e);
                return;
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                log.info("MFC Error 3: ", e);
                return;
            }
        }
    }
}
