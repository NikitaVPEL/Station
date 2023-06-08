//package com.vst.station.configuration;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.mongodb.MongoClient;
//
//import jakarta.annotation.PreDestroy;
//
//@Configuration
//public class MongoDbConfiguration {
//
//	public static final Logger logger = LogManager.getLogger(MongoDbConfiguration.class);
//
//	private MongoClient mongoClient;
//
//	@Bean
//	public MongoClient mongoClient() {
//		if (mongoClient == null) {
//			logger.info("Connection Established");
//			mongoClient = new MongoClient();
//		}
//		return mongoClient;
//	}
//
//	@PreDestroy
//	public void cleanUp() {
//		if (mongoClient != null) {
//			mongoClient.close();
//			logger.info("Connection Closed");
//		}
//	}
//}
