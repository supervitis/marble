package org.marble.commons.config;

import com.mongodb.MongoClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

@PropertySource(value = "classpath:db.properties")
@Configuration
public class MongoDbConfig {

    @Autowired
    Environment env;

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
}
