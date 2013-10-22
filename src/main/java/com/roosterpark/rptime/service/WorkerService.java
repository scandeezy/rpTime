package com.roosterpark.rptime.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roosterpark.rptime.model.Worker;

@Singleton
public class WorkerService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	public Worker getById(Long id) {
		LOGGER.warn("Getting worker with key " + id);
		return ofy().load().type(Worker.class).id(id).now();
	}
	
	public Worker getByEmail(String email) {
		LOGGER.warn("Getting worker with email " + email);
		return ofy().load().type(Worker.class).filter(Worker.EMAIL_KEY, email).first().now();
	}

	public List<Worker> getPage(int count, int offset) {
		LOGGER.warn("Getting worker page with count " + count + " and offset " + offset);
		return ofy().load().type(Worker.class).limit(count).offset(offset).list();
	}
	
	public List<Worker> getAll() {
		return ofy().load().type(Worker.class).list();
	}

	public void set(Worker item) {
		LOGGER.warn("Saving worker " + item);
		ofy().save().entity(item).now();
	}
}