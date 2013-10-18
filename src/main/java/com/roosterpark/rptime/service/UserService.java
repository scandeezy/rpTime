package com.roosterpark.rptime.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.roosterpark.rptime.model.User;

@Singleton
public class UserService implements Service<User> {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Override
	public User get(String sKey) throws EntityNotFoundException {
		LOGGER.warn("Getting user with key " + sKey);
		Long key = Long.valueOf(sKey);
		return ofy().load().type(User.class).id(key).now();
	}

	@Override
	public List<User> getPage(int count, int offset) {
		LOGGER.warn("Getting user page with count " + count + " and offset " + offset);
		return ofy().load().type(User.class).limit(count).offset(offset).list();
	}

	@Override
	public void set(User item) {
		LOGGER.warn("Saving user " + item);
		ofy().save().entity(item).now();
	}
}
