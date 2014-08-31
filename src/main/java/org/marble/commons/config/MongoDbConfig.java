package org.marble.commons.config;

import com.mongodb.MongoClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoExceptionTranslator;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@PropertySource(value = "classpath:db.properties")
@Configuration
public class MongoDbConfig {

    @Autowired
    Environment env;
    
    @Autowired
    LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean;

    // MongoDB cross-store
    public @Bean
    MongoDbFactory mongoDbFactory() throws Exception {
        return new SimpleMongoDbFactory(new MongoClient(),
                env.getProperty("mongodb.database"),
                new UserCredentials(env.getProperty("mongodb.username"),
                        env.getProperty("mongodb.password")));
    }

    public @Bean
    MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        return mongoTemplate;
    }
    
    /* This was used for cross-store MongoDB, but it is unusable right now 
    @Bean
    public MongoDocumentBacking mongoDocumentBacking() throws Exception {
        MongoDocumentBacking mdb = MongoDocumentBacking.aspectOf();
        mdb.setChangeSetPersister(changeSetPersister());
        return mdb;
    }

    @Bean
    public MongoChangeSetPersister changeSetPersister() throws Exception {
        MongoChangeSetPersister mongoChangeSetPersister = new MongoChangeSetPersister();
        mongoChangeSetPersister.setEntityManagerFactory(localContainerEntityManagerFactoryBean.getObject());
        mongoChangeSetPersister.setMongoTemplate(mongoTemplate());
        return mongoChangeSetPersister;
    }

    @Bean
    public MongoExceptionTranslator mongoExceptionTranslator() {
        return new MongoExceptionTranslator();
    }
    /*

    public @Bean
    MongoExceptionTranslator mongoExceptionTranslator() {
        return new MongoExceptionTranslator();
    }

    public @Bean
    MongoDocumentBacking mongoDocumentBacking(MongoChangeSetPersister mongoChangeSetPersister) {
        MongoDocumentBacking mongoDocumentBacking = MongoDocumentBacking.aspectOf();
        mongoDocumentBacking.setChangeSetPersister(mongoChangeSetPersister);
        return mongoDocumentBacking;

    }

    public @Bean
    MongoChangeSetPersister mongoChangeSetPersister(EntityManagerFactory entityManagerFactory,
            MongoTemplate mongoTemplate) {
        MongoChangeSetPersister mongoChangeSetPersister = new MongoChangeSetPersister();
        mongoChangeSetPersister.setMongoTemplate(mongoTemplate);
        mongoChangeSetPersister.setEntityManagerFactory(entityManagerFactory);
        return mongoChangeSetPersister;
    }
    */

}
