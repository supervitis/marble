package org.marble.commons.dao.model;

import java.util.Date;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterObjectFactory;
import twitter4j.User;

public class ProcessedStatusTest {
    
    @DataProvider(name="processedStatus")    
    public Object[][] createProcessedStatus() {
        ProcessedStatus processedStatus = new ProcessedStatus();
        processedStatus.setCreatedAt(new Date(1286705410000L));
        processedStatus.setId(123L);
        processedStatus.setOriginalCreatedAt(new Date(1286705411000L));
        processedStatus.setPolarity(0.45F);
        processedStatus.setRetweeted(true);
        processedStatus.setScreenName("screenName");
        processedStatus.setText("textOfTheProcessedStatus");
        processedStatus.setTimeZone("timeZone");
        processedStatus.setTopicId(678);
        return new Object[][] { new Object[] { processedStatus } };
    }

    @DataProvider(name="originalStatus")    
    public Object[][] createOriginalStatus() throws TwitterException {
        
        
        OriginalStatus originalStatus = new OriginalStatus();
        originalStatus.setCreatedAt(new Date(1286705410000L));
        originalStatus.setId(123L);
        originalStatus.setRetweeted(true);
        originalStatus.setUser(null);
        originalStatus.setText("textOfTheProcessedStatus");
        
        OriginalStatus retweetedStatus = new OriginalStatus();
        retweetedStatus.setCreatedAt(new Date(1286705411000L));
        originalStatus.setRetweetedStatus(retweetedStatus);
        
        originalStatus.setTopicId(678);
        return new Object[][] { new Object[] { originalStatus } };
    }
    
    @Test(dataProvider="processedStatus")
    public void ProcessedStatusFromProcessedStatus(ProcessedStatus processedStatus) {
        ProcessedStatus newProcessedStatus = new ProcessedStatus(processedStatus);
        Assert.assertEquals(newProcessedStatus.getCreatedAt(), processedStatus.getCreatedAt());
        Assert.assertEquals(newProcessedStatus.getId(), processedStatus.getId());
        Assert.assertEquals(newProcessedStatus.getOriginalCreatedAt(), processedStatus.getOriginalCreatedAt());
        Assert.assertEquals(newProcessedStatus.getPolarity(), processedStatus.getPolarity());
        Assert.assertEquals(newProcessedStatus.isRetweeted(), processedStatus.isRetweeted());
        Assert.assertEquals(newProcessedStatus.getScreenName(), processedStatus.getScreenName());
        Assert.assertEquals(newProcessedStatus.getText(), processedStatus.getText());
        Assert.assertEquals(newProcessedStatus.getTimeZone(), processedStatus.getTimeZone());
        Assert.assertEquals(newProcessedStatus.getTopicId(), processedStatus.getTopicId());
    }

    @Test(dataProvider="originalStatus")
    public void ProcessedStatusFromOriginalStatus(OriginalStatus originalStatus) {
        ProcessedStatus newProcessedStatus = new ProcessedStatus(originalStatus);
        Assert.assertEquals(newProcessedStatus.getCreatedAt(), originalStatus.getCreatedAt());
        Assert.assertEquals(newProcessedStatus.getId(), originalStatus.getId());
        Assert.assertEquals(newProcessedStatus.getOriginalCreatedAt(), originalStatus.getRetweetedStatus().getCreatedAt());
        Assert.assertEquals(newProcessedStatus.getPolarity(), null);
        Assert.assertEquals(newProcessedStatus.isRetweeted(), originalStatus.isRetweeted());
        Assert.assertEquals(newProcessedStatus.getText(), originalStatus.getText());
        Assert.assertEquals(newProcessedStatus.getTopicId(), originalStatus.getTopicId());
    }
}
