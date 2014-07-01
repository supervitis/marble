package org.marble.service.processors;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.marble.util.Constants;
import org.marble.data.GlobalConfigurationRepository;
import org.marble.data.datastore.MongoDatastoreOperations;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@RequestScoped
public class SenticCalculator implements PolarityCalculator {

    private Map<String, SenticItem> senticMap;
    
    @Inject
    private MongoDatastoreOperations mongoDatastoreOperations;

    @Inject
    private GlobalConfigurationRepository globalConfigurationRepository;
    
    @PostConstruct
    public void populate() {
        
        // Load all the fields here
        String collection = globalConfigurationRepository.getValueByName(Constants.MONGO_SENTICNET_COLLECTION);

        DBCursor cursor = (DBCursor) mongoDatastoreOperations.getCollectionData(collection);

        this.senticMap = new HashMap<>();

        while (cursor.hasNext()) {
            DBObject item = cursor.next();
            String text = (String) item.get("text");
            Float polarity = Float.parseFloat((String) item.get("polarity"));
            SenticItem senticItem = new SenticItem(text, polarity);
            this.senticMap.put(text, senticItem);

        }

    }

    @Override
    public Float calculatePolarity(String sentence) {

        Float polarity = null;

        SenticItem senticItem = this.senticMap.get(sentence);
        if (senticItem != null) {
            polarity = senticItem.getPolarity();
        }

        return polarity;

    }
}
