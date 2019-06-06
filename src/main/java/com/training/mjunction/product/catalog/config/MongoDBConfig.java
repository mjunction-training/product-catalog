package com.training.mjunction.product.catalog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
@EnableMongoRepositories(basePackages = "com.training.mjunction.product.catalog.data")
public class MongoDBConfig {

	@Bean
	public MongoClient mongoClient(@Value("${mongodb.db.host:localhost}") final String mongoDbHostName,
			@Value("${mongodb.db.port:27017}") final int mongoDbPort) {
		return MongoClients.create(String.format("mongodb://%s:%d", mongoDbHostName, mongoDbPort));
	}

	@Bean
	public MongoTemplate mongoTemplate(@Value("${mongodb.db.name:product}") final String databaseName,
			final MongoClient mongoClient) {
		return new MongoTemplate(mongoClient, databaseName);
	}

}
