package com.roosterpark.rptime.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.roosterpark.rptime.model.Company;
import com.roosterpark.rptime.model.User;

@Singleton
public class UserService implements Service<User> {
	private static final Logger LOG = Logger.getLogger(UserService.class.getName());

	public static final String USER_BUCKET_KEY = "users";
	public static final String FIRST_NAME_KEY = "firstName";
	public static final String LAST_NAME_KEY = "lastName";
	public static final String EMAIL_KEY = "email";
	public static final String START_DATE_KEY = "startDate";
	
	@Inject
	DatastoreService datastore;
	@Override
	public User get(String sKey) throws EntityNotFoundException {
		Key key = KeyFactory.createKey(USER_BUCKET_KEY, sKey);
		Entity entity;
		entity = datastore.get(key);
		LOG.warning("Entity extracted: " + entity);
		User user = toUser(entity);
		LOG.warning("Turned into company: " + user);
		return user;
	}

	@Override
	public List<User> getPage(int count, int offset) {
		Query query = new Query(USER_BUCKET_KEY);
		List<Entity> entities = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(count).offset(offset));

		List<User> users = new LinkedList<User>();
		for (Entity entity : entities) {
			LOG.warning("Entity extracted: " + entity);
			User user = toUser(entity);
			users.add(user);
			LOG.warning("Turned into company: " + user);
		}

		return users;
	}

	@Override
	public void set(User item) {
		LOG.warning("Translating company to entity: " + item);
		Entity company = toEntity(item);
		LOG.warning("Setting entity: " + company);
		datastore.put(company);
	}
	
	private User toUser(Entity entity) {
		User user = new User();
		
		user.setFirstName((String)entity.getProperty(FIRST_NAME_KEY));
		user.setLastName((String)entity.getProperty(LAST_NAME_KEY));
		user.setEmail((String)entity.getProperty(EMAIL_KEY));
		user.setStart((Date)entity.getProperty(START_DATE_KEY));
		
		return user;
	}
	
	private Entity toEntity(User user) {
		if (user == null)
			throw new IllegalArgumentException("No arguments may be null.");
		
		Entity entity = new Entity(USER_BUCKET_KEY);

		entity.setProperty(FIRST_NAME_KEY, user.getFirstName());
		entity.setProperty(LAST_NAME_KEY, user.getLastName());
		entity.setProperty(EMAIL_KEY, user.getEmail());
		entity.setProperty(START_DATE_KEY, user.getStart());

		return entity;
	}
}
