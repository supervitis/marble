package org.marble.data;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.marble.data.datastore.MongoDatastoreOperations;
import org.marble.model.Topic;

@RequestScoped
public class TopicListProducer {

    @Inject
    private TopicRepository    topicRepository;

    private List<Topic>        topicList;
    
    private List<Topic>        topicListWithStatus;

    private Map<String, Topic> topicMap;
    
    @Inject
    private MongoDatastoreOperations mongoDatastoreOperations;
    
    //@Inject
    //private Logger log;

    @Produces
    @Named
    public List<Topic> getTopicList() {
        return topicList;
    }

    public List<Topic> getTopicListWithStatus() {
        for (Topic topic : topicList) {
            // TODO This is not persistent. Need to find a way to make it persistent.
            String collection = topic.getName();
            mongoDatastoreOperations.setCollection(collection);
            topic.getTopicStatus().setDatabaseStatus(mongoDatastoreOperations.getMongoStatus());
        }
        topicMap = new HashMap<String, Topic>();
        for (Topic i : topicList)
            topicMap.put(i.getName(), i);
        return topicListWithStatus;
    }

    public void onTopicListChanged(
            @Observes(notifyObserver = Reception.IF_EXISTS) final Topic topicList) {
        generateList();
    }

    @Named
    public Topic getSpecificTopic(String name) {
        return topicRepository.getByName(name);
    }

    @Produces
    @Named
    public Map<String, Topic> getTopicMap() {
        return topicMap;
        
    }

    @PostConstruct
    public void generateList() {
        topicList = topicRepository.getAllOrderedByName();
        topicMap = new HashMap<String, Topic>();
        for (Topic i : topicList)
            topicMap.put(i.getName(), i);
    }

}
