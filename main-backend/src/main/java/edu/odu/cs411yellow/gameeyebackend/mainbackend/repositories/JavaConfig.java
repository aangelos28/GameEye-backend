package edu.odu.cs411yellow.gameeyebackend.mainbackend.repositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.mapping.event.LoggingEventListener;

public class JavaConfig {
    public @Bean MongoClient mongoClient() {
        return MongoClients.create("mongodb://dbAdmin:*****@411yellow.cpi.cs.odu.edu:27017/?authSource=admin&readPreference=primary&appname=MongoDB%20Compass&ssl=false");
    }
}
