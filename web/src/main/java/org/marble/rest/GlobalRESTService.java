package org.marble.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.marble.service.plotters.Histogram;
import org.marble.service.statistics.StatisticsExtractor;
import org.marble.util.Constants;

@Path("/")
@RequestScoped
public class GlobalRESTService {
    @Inject
    private Histogram histogram;
    
    @Inject
    private StatisticsExtractor statistics;

    @GET
    @Path("/plotter/histogram/statuses")
    @Produces(MediaType.APPLICATION_JSON)
    public String getStatusesHistogram(@QueryParam("name") String topicName,
            @DefaultValue(Constants.PLOTTER_TOTAL) @QueryParam("statusTypes") String statusTypes) {
        return histogram.getFlotStatusesChart(topicName, statusTypes);
    }

    @GET
    @Path("/plotter/histogram/polarities")
    @Produces(MediaType.APPLICATION_JSON)
    public String getPolaritiesHistogram(@QueryParam("name") String topicName,
            @DefaultValue(Constants.PLOTTER_TOTAL) @QueryParam("statusTypes") String statusTypes) {
        return histogram.getFlotPolaritiesChart(topicName, statusTypes);
    }
    
    @GET
    @Path("/statistics/topic/global")
    @Produces(MediaType.APPLICATION_JSON)
    public String getGlobalTopicStatistics(@QueryParam("name") String topicName) {
        return statistics.getGlobalTopicStatistics(topicName);
    }
    
    @GET
    @Path("/statistics/topic/detailed")
    @Produces(MediaType.APPLICATION_JSON)
    public String getDetailedTopicStatistics(@QueryParam("name") String topicName) {
        return statistics.getDetailedTopicStatistics(topicName);
    }
}
