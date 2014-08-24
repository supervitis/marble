package org.marble.commons.thread;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.marble.commons.dao.model.Execution;
import org.marble.commons.exception.InvalidExecutionException;
import org.marble.commons.service.ExecutionService;
import org.marble.commons.service.ResetServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Executor implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(Executor.class);

    @Autowired
    ExecutionService executionService;

    Integer id;

    public Executor() {

    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public void run() {

        Execution execution;
        try {
            log.info("MFC: lastid dentro: " + id);
            log.info("MFC: exec: " + executionService);
            execution = executionService.getExecution(id);
        } catch (InvalidExecutionException e) {
            log.info("MFC Error 1: ", e);
            return;
        }

        for (int i = 0; i < 20; i++) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date = dateFormat.format(new Date());

            log.info("MFC: " + date + ": process <"+id+"> is running.");
            execution.appendLog(date + ": process is running.\n");
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
