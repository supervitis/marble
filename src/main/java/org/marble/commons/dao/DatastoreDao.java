/*package org.marble.commons.dao;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.marble.model.Topic;
import org.marble.util.Constants;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class DatastoreDao {

    private String collection = null;

    @Inject
    private MongoConnector mongoConnection;
    @Inject
    private Logger log;

    public MongoDatastoreOperations() {

    }

    public MongoDatastoreOperations(String collection) {
        this.setCollection(collection);
    }

    @Override
    public void setCollection(String collection) {
        this.collection = collection;
    }

    public void renameCollection(String newName) {
        // MongoConnector mongoConnection = MongoConnector.getInstance();
        DB mongo = mongoConnection.getDbClient();
        DBCollection dbCollection = mongo.getCollection(this.collection);
        dbCollection.rename(newName);
    }

    @Override
    public void insertOriginalStatus(String status) {
        //MongoConnector mongoConnection = MongoConnector.getInstance();
        DB mongo = mongoConnection.getDbClient();
        log.trace("iOS: Collection: " + this.collection + ", DB: " + mongo.getName());
        log.trace("Text: " + status);
        DBObject dbObject = (DBObject) JSON.parse(status);
        if (dbObject.containsField("id_str") && dbObject.containsField("created_at")) {
        DBCollection dbCollection = mongo.getCollection(this.collection);
        dbCollection.insert(dbObject);
        }

    }

    @Override
    public DBCursor getOriginalStatuses() {
        // MongoConnector mongoConnection = MongoConnector.getInstance();
        DB mongo = mongoConnection.getDbClient();
        log.trace("gOS: Collection: " + this.collection + ", DB: "
                + mongo.getName());
        DBCollection dbCollection = mongo.getCollection(this.collection);
        DBCursor cursor = dbCollection.find();
        return cursor;
    }

    public DBCursor getCollectionData(String collection) {
        // MongoConnector mongoConnection = MongoConnector.getInstance();
        DB mongo = mongoConnection.getDbClient();
        log.trace("gCD: Collection: " + collection + ", DB: " + mongo.getName());
        DBCollection dbCollection = mongo.getCollection(collection);
        DBCursor cursor = dbCollection.find();
        return cursor;
    }

    public void insertProcessedPolarity(String processedPolarity) {
        // MongoConnector mongoConnection = MongoConnector.getInstance();
        DB mongo = mongoConnection.getDbClient();
        String collection = this.collection + Constants.MONGO_PROCESSED_SUFFIX;
        log.trace("iOS: Collection: " + collection + ", DB: " + mongo.getName());
        log.trace("Text: " + processedPolarity);
        DBObject dbObject = (DBObject) JSON.parse(processedPolarity);
        DBCollection dbCollection = mongo.getCollection(collection);
        dbCollection.insert(dbObject);
    }

    public Object getProcessedData() {
        // MongoConnector mongoConnection = MongoConnector.getInstance();
        DB mongo = mongoConnection.getDbClient();
        String collection = this.collection + Constants.MONGO_PROCESSED_SUFFIX;
        log.trace("gPP: Collection: " + collection + ", DB: " + mongo.getName());
        DBCollection dbCollection = mongo.getCollection(collection);
        DBCursor cursor = dbCollection.find();
        return cursor;
    }

    public Map<String, String> getMongoStatus() {
        // MongoConnector mongoConnection = MongoConnector.getInstance();
        DB mongo = mongoConnection.getDbClient();

        Map<String, String> status = new HashMap<String, String>();
        status.put("Collection Exists",
                String.valueOf(mongo.collectionExists(collection)));
        status.put(
                "Is Processed",
                String.valueOf(mongo.collectionExists(collection
                        + Constants.MONGO_PROCESSED_SUFFIX)));

        DBCollection dbCollection = mongo.getCollection(this.collection);

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
        dbCollection = mongo.getCollection(this.collection
                + Constants.MONGO_PROCESSED_SUFFIX);
        status.put("Total Status Processed Count",
                String.valueOf(dbCollection.count()));

        return new TreeMap<String, String>(status);
    }

    public Boolean getExistsCollection() {
        // MongoConnector mongoConnection = MongoConnector.getInstance();
        DB mongo = mongoConnection.getDbClient();
        return mongo.collectionExists(collection);
    }

    public Boolean isProcessed() {
        // MongoConnector mongoConnection = MongoConnector.getInstance();
        DB mongo = mongoConnection.getDbClient();
        return mongo.collectionExists(collection
                + Constants.MONGO_PROCESSED_SUFFIX);

    };

    public String getOldestStatusDate() {
        // MongoConnector mongoConnection = MongoConnector.getInstance();
        DB mongo = mongoConnection.getDbClient();
        DBCollection dbCollection = mongo.getCollection(this.collection);
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
        // MongoConnector mongoConnection = MongoConnector.getInstance();
        DB mongo = mongoConnection.getDbClient();
        DBCollection dbCollection = mongo.getCollection(this.collection);
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
        // MongoConnector mongoConnection = MongoConnector.getInstance();
        DB mongo = mongoConnection.getDbClient();
        DBCollection dbCollection = mongo.getCollection(this.collection);
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
        // MongoConnector mongoConnection = MongoConnector.getInstance();
        DB mongo = mongoConnection.getDbClient();
        DBCollection dbCollection = mongo.getCollection(this.collection);
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
        // MongoConnector mongoConnection = MongoConnector.getInstance();
        DB mongo = mongoConnection.getDbClient();
        DBCollection dbCollection = mongo.getCollection(this.collection
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
                        this.insertOriginalStatus(sCurrentLine);
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

}
*/