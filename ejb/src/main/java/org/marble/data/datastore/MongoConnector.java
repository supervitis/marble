package org.marble.data.datastore;

import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.log4j.Logger;
import org.marble.util.Constants;
import org.marble.data.GlobalConfigurationRepository;
//import org.marble.processors.BasicProcessor;


import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.util.StringParseUtil;

@Singleton
public class MongoConnector extends DatastoreConnection {

    static Logger                         log = Logger.getLogger(MongoConnector.class.getName());

    @Inject
    private GlobalConfigurationRepository globalConfigurationRepository;

    @PostConstruct
    private void connect() {

        String hostname = globalConfigurationRepository.getValueByName(Constants.MONGO_HOSTNAME);
        Integer mongo_port = StringParseUtil.parseInt(
                globalConfigurationRepository.getValueByName(Constants.MONGO_PORT), 0);
        String username = globalConfigurationRepository.getValueByName(Constants.MONGO_USERNAME);
        String password = globalConfigurationRepository.getValueByName(Constants.MONGO_PASSWORD);
        String database = globalConfigurationRepository.getValueByName(Constants.MONGO_DATABASE);

        // String hostname = "localhost";
        // Integer mongo_port = 27017;
        // String username = null;
        // String password = null;
        // String database = "Marble-Initiative";

        // private constructor
        try {
            MongoClient client = new MongoClient(hostname, mongo_port);
            this.connection = client.getDB(database);
            if (username != null && password != null) {
                boolean auth = connection.authenticate(username, password.toCharArray());
                if (auth) {
                    log.debug("Login is successful!");
                } else {
                    log.error("Login is incorrect!");
                }
            }
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static MongoConnector myInstance;

    private DB                   connection;

    public static class ConnectionHolder {
        // FUTURE In the future this scheme could be replaced by a new using a
        // connection pool

        public static MongoConnector connection = new MongoConnector();

        // TODO Verify that the connection is up, start it up if down
    }

    public static MongoConnector getInstance() {
        return ConnectionHolder.connection;
    }

    public DB getDbClient() {
        return this.connection;
    }

}