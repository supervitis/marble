package org.marble.commons.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import org.marble.commons.dao.model.OriginalStatus;

@Service
public class DatastoreServiceImpl implements DatastoreService {

    @Autowired
    MongoOperations mongoOperations;

    private static final Logger log = LoggerFactory.getLogger(DatastoreServiceImpl.class);

    public void insertOriginalStatus(OriginalStatus originalStatus) {
        mongoOperations.save(originalStatus);
    }

    /*public DatastoreServiceImpl(String collection) {
        this.setCollection(collection);
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public void renameCollection(String newName) {
        DBCollection dbCollection = mongoOperations.getCollection(this.collection);
        dbCollection.rename(newName);
    }

   
    public DBCursor getOriginalStatuses() {
        DBCollection dbCollection = mongoOperations.getCollection(this.collection);
        DBCursor cursor = dbCollection.find();
        return cursor;
    }

    public DBCursor getCollectionData(String collection) {
        DBCollection dbCollection = mongoOperations.getCollection(collection);
        DBCursor cursor = dbCollection.find();
        return cursor;
    }

    public void insertProcessedPolarity(String processedPolarity) {
        String collection = this.collection + Constants.MONGO_PROCESSED_SUFFIX;
        DBObject dbObject = (DBObject) JSON.parse(processedPolarity);
        DBCollection dbCollection = mongoOperations.getCollection(collection);
        dbCollection.insert(dbObject);
    }

    public Object getProcessedData() {
        String collection = this.collection + Constants.MONGO_PROCESSED_SUFFIX;
        DBCollection dbCollection = mongoOperations.getCollection(collection);
        DBCursor cursor = dbCollection.find();
        return cursor;
    }

    public Map<String, String> getMongoStatus() {
        Map<String, String> status = new HashMap<String, String>();
        DBCollection dbCollection = mongoOperations.getCollection(this.collection);

        DBCursor cursor = dbCollection.find()
                .sort(new BasicDBObject("id_str", 1)).limit(1);
        while (cursor.hasNext()) {
            DBObject dbObject = cursor.next();
            try {
                status.put("Oldest Status Date", dbObject.get("created_at")
                        .toString());
                status.put("Oldest Status ID", dbObject.get("id_str")
                        .toString());
            } catch (Exception e) {
                log.error("The oldest message doesn't have correct info for created_at or id_str.");
            }
            break;
        }

        cursor = dbCollection.find().sort(new BasicDBObject("id_str", -1))
                .limit(1);
        while (cursor.hasNext()) {
            DBObject dbObject = cursor.next();
            try {
                status.put("Newest Status Date", dbObject.get("created_at")
                        .toString());
                status.put("Newest Status ID", dbObject.get("id_str")
                        .toString());
            } catch (Exception e) {
                log.error("The newest message doesn't have correct info for created_at or id_str.");
            }
            break;
        }
        status.put("Total Status Count", String.valueOf(dbCollection.count()));

        // TODO Separate and reorder
        dbCollection = mongoOperations.getCollection(this.collection
                + Constants.MONGO_PROCESSED_SUFFIX);
        status.put("Total Status Processed Count",
                String.valueOf(dbCollection.count()));

        return new TreeMap<String, String>(status);
    }

    public Boolean getExistsCollection() {
        return mongoOperations.collectionExists(collection);
    }

    public Boolean isProcessed() {
        return mongoOperations.collectionExists(collection
                + Constants.MONGO_PROCESSED_SUFFIX);

    };

    public String getOldestStatusDate() {
        DBCollection dbCollection = mongoOperations.getCollection(this.collection);
        DBCursor cursor = dbCollection.find()
                .sort(new BasicDBObject("id_str", 1)).limit(1);
        String data = null;
        while (cursor.hasNext()) {
            DBObject dbObject = cursor.next();
            data = dbObject.get("created_at").toString();
        }
        return data;
    }

    public String getOldestStatusId() {
        DBCollection dbCollection = mongoOperations.getCollection(this.collection);
        DBCursor cursor = dbCollection.find()
                .sort(new BasicDBObject("id_str", 1)).limit(1);
        String data = null;
        while (cursor.hasNext()) {
            DBObject dbObject = cursor.next();
            data = dbObject.get("id_str").toString();
        }
        return data;
    }

    public String getNewestStatusDate() {
        DBCollection dbCollection = mongoOperations.getCollection(this.collection);
        DBCursor cursor = dbCollection.find()
                .sort(new BasicDBObject("id_str", -1)).limit(1);
        String data = null;
        while (cursor.hasNext()) {
            DBObject dbObject = cursor.next();
            data = dbObject.get("created_at").toString();
        }
        return data;
    }

    public String getNewestStatusId() {
        DBCollection dbCollection = mongoOperations.getCollection(this.collection);
        DBCursor cursor = dbCollection.find()
                .sort(new BasicDBObject("id_str", -1)).limit(1);
        String data = null;
        while (cursor.hasNext()) {
            DBObject dbObject = cursor.next();
            data = dbObject.get("id_str").toString();
        }
        return data;
    }

    public void dropProcessedCollection() {
        DBCollection dbCollection = mongoOperations.getCollection(this.collection
                + Constants.MONGO_PROCESSED_SUFFIX);
        dbCollection.drop();
    }

    public void insertDataFromFile(Topic topic, File file) throws Exception {
        Boolean errorFlag = Boolean.FALSE;
        Exception ex = null;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                try {
                    sCurrentLine = sCurrentLine.replaceFirst(", $", "");
                    if (sCurrentLine != null && !sCurrentLine.isEmpty()) {
                        //TODO MFC this.insertOriginalStatus(sCurrentLine);
                    }
                } catch (Exception e) {
                    errorFlag = Boolean.TRUE;
                    ex = e;
                    log.error("Error inserting Data from file.", e);
                }
            }
        } catch (Exception e) {
            log.error("Error inserting Data from file.", e);
            throw (e);
        }
        if (errorFlag) {
            throw (ex);
        }
    }
    */

}