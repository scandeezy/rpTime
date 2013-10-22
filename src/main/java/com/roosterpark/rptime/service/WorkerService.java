package com.roosterpark.rptime.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.ObjectifyService;
import com.roosterpark.rptime.model.Worker;

@Named
public class WorkerService implements Service<Worker> {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	public WorkerService() {
		LOGGER.trace("registering Worker class with ObjectifyService");
		ObjectifyService.register(Worker.class);
		LOGGER.trace("registered Worker");
	}

	@Override
	public Worker get(String sKey) throws EntityNotFoundException {
		LOGGER.warn("Getting user with key " + sKey);
		Long key = Long.valueOf(sKey);
		return ofy().load().type(Worker.class).id(key).now();
	}

	@Override
	public List<Worker> getPage(int count, int offset) {
		LOGGER.warn("Getting user page with count " + count + " and offset " + offset);
		return ofy().load().type(Worker.class).limit(count).offset(offset).list();
	}

	@Override
	public void set(Worker item) {
		LOGGER.warn("Saving user " + item);
		ofy().save().entity(item).now();
	}
}
