package com.roosterpark.rptime.config;

import javax.inject.Named;

import org.springframework.context.annotation.Configuration;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@Configuration
public class DatastoreConfiguration {

	@Named
	public DatastoreService datastoreService() {
		return DatastoreServiceFactory.getDatastoreService();
	}

	@Named
	public UserService userService() {
		return UserServiceFactory.getUserService();
	}
}
