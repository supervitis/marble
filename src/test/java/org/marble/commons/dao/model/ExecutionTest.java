package org.marble.commons.dao.model;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

import org.marble.commons.util.MarbleUtil;

public class ExecutionTest {

    
    //private Execution execution = new Execution();

    @DataProvider(name="executionObject")
    public Object[][] createExecutionObject() {
        Date mockDate = new Date(1286705410000L); // 2010-10-10-10:10:10
        Execution execution = new Execution();
        execution.setCreatedAt(mockDate);
        execution.setUpdatedAt(mockDate);
        execution.setLog("END");
        return new Object[][] { new Object[] { execution } };
    }

    @Test(dataProvider="executionObject")
    public void appendLog(Execution execution) {
        String previousLog = execution.getLog();
        String appendString = "EXTRASTRING";
        String expectedLog = MarbleUtil.getDatedMessage(appendString) + "\n" + previousLog;
        execution.appendLog(appendString);
        Assert.assertEquals(execution.getLog(),expectedLog);
        
        for (int i = 0; i < Execution.LOG_LIMIT; i = i + 50) {
            String singleLine = StringUtils.repeat("abc", 50);
            expectedLog = MarbleUtil.getDatedMessage(singleLine) + "\n" + expectedLog;
            execution.appendLog(singleLine); 
        }
        String receivedLog = execution.getLog();
        receivedLog = receivedLog.replaceAll("^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}", "XXXX-XX-XX XX:XX:XX");
        
        expectedLog = expectedLog.substring(0, Execution.LOG_LIMIT);
        expectedLog = expectedLog.substring(0, expectedLog.lastIndexOf("\n"));
        expectedLog = expectedLog.replaceAll("^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}", "XXXX-XX-XX XX:XX:XX");
        Assert.assertEquals(receivedLog,expectedLog);
    }

    @Test(dataProvider="executionObject")
    public void prePersist(Execution execution) {
        Date newDate = new Date();
        execution.prePersist();
        Assert.assertTrue(!execution.getCreatedAt().before(newDate) && !execution.getUpdatedAt().before(newDate));
    }

    @Test(dataProvider="executionObject")
    public void preUpdate(Execution execution) {
        Date newDate = new Date();
        execution.preUpdate();
        Assert.assertTrue(!execution.getUpdatedAt().before(newDate));
    }
}
