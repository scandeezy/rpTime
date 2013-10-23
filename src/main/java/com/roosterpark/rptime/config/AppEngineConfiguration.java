package com.roosterpark.rptime.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@Configuration
public class AppEngineConfiguration {
	Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Bean
	public DatastoreService datastoreService() {
		LOGGER.debug("init DataStoreService");
		return DatastoreServiceFactory.getDatastoreService();
	}

	@Bean
	public UserService userService() {
		LOGGER.debug("init UserService");
		return UserServiceFactory.getUserService();
	}
}
