package com.roosterpark.rptime.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Singleton;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.roosterpark.rptime.model.User;

@Singleton
public class UserService implements Service<User> {
	private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

	public static final String EMAIL_KEY = "email";
	
	@Override
	public User get(String sKey) throws EntityNotFoundException {
		LOGGER.warning("Getting entity with key " + sKey);
		return ofy().load().type(User.class).filter(EMAIL_KEY, sKey).first().now();
	}

	@Override
	public List<User> getPage(int count, int offset) {
		LOGGER.warning("Getting user page with count " + count + " and offset " + offset);
		return ofy().load().type(User.class).limit(count).offset(offset).list();
	}

	@Override
	public void set(User item) {
		LOGGER.warning("Saving user " + item);
		ofy().save().entity(item).now();
	}
}
