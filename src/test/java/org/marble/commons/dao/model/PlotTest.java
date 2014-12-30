package org.marble.commons.dao.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

public class PlotTest {

    @DataProvider(name = "validData")
    public Object[][] createValidData() {
        List<Map<String, Object>> data = new ArrayList<>();
        HashMap<String, Object> firstMap = new HashMap<>();
        firstMap.put("Name1", "Value1");
        firstMap.put("Name2", "Value2");
        data.add(firstMap);
        HashMap<String, Object> secondMap = new HashMap<>();
        secondMap.put("Name3", "Value3");
        secondMap.put("Name4", "Value4");
        data.add(secondMap);

        String expectedData = "[{\"Name2\":\"Value2\",\"Name1\":\"Value1\"},{\"Name4\":\"Value4\",\"Name3\":\"Value3\"}]";
        return new Object[][] { new Object[] { data, expectedData } };
    }
    
    @DataProvider(name = "validOptions")
    public Object[][] createValidOptions() {
        HashMap<String, Object> firstMap = new HashMap<>();
        firstMap.put("Name1", "Value1");
        firstMap.put("Name2", "Value2");
        firstMap.put("Name3", "Value3");
        firstMap.put("Name4", "Value4");

        String expectedData = "{\"Name4\":\"Value4\",\"Name3\":\"Value3\",\"Name2\":\"Value2\",\"Name1\":\"Value1\"}";
        return new Object[][] { new Object[] { firstMap, expectedData } };
    }

    @Test(dataProvider = "validData")
    public void setData(List<Map<String, Object>> data, String expectedData) {
        Plot plot = new Plot();
        plot.setData(data);
        String receivedData = plot.getData();
        Assert.assertEquals(receivedData, expectedData);
    }

    @Test(dataProvider = "validOptions")
    public void setMainOptions(Map<String, Object> options, String expectedOptions) {
        Plot plot = new Plot();
        plot.setMainOptions(options);
        String receivedOptions = plot.getMainOptions();
        Assert.assertEquals(receivedOptions, expectedOptions);
    }

    @Test(dataProvider = "validOptions")
    public void setOverviewOptions(Map<String, Object> options, String expectedOptions) {
        Plot plot = new Plot();
        plot.setOverviewOptions(options);
        String receivedOptions = plot.getOverviewOptions();
        Assert.assertEquals(receivedOptions, expectedOptions);
    }
}
