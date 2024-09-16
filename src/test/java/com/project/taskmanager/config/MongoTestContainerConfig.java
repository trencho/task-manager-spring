package com.project.taskmanager.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MongoDBContainer;

@TestConfiguration
public class MongoTestContainerConfig {

    @Bean
    public MongoDBContainer mongoDBContainer() {
        MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");
        mongoDBContainer.start();
        return mongoDBContainer;
    }

}
