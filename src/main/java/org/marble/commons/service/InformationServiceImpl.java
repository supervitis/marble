package org.marble.commons.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.marble.commons.dao.model.OriginalStatus;
import org.marble.commons.model.HomeInformation;

@Service
public class InformationServiceImpl implements InformationService {
    
    @Autowired
    TopicService topicService;
    @Autowired
    ExecutionService executionService;
    @Autowired
    DatastoreService datastoreService;
    @Autowired
    PlotService plotService;

    @Override
    public HomeInformation getHomeInformation() {
        HomeInformation homeInformation = new HomeInformation();
        homeInformation.setTopics(topicService.count());
        homeInformation.setExecutions(executionService.count());
        homeInformation.setPlots(plotService.count());
        homeInformation.setStatuses(datastoreService.countAll(OriginalStatus.class));
        return homeInformation;
    }
}
