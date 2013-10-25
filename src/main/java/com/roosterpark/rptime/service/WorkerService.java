package com.roosterpark.rptime.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.users.User;
import com.roosterpark.rptime.model.Worker;

@Named
public class WorkerService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	// public WorkerService() {
	// LOGGER.trace("registering Worker class with ObjectifyService");
	// ObjectifyService.register(Worker.class);
	// LOGGER.trace("registered Worker");
	// }

	public Worker getById(Long id) {
		LOGGER.warn("Getting worker with id={}", id);
		return ofy().load().type(Worker.class).id(id).now();
	}

	public Worker getByEmail(String email) {
		LOGGER.warn("Getting worker with email={} ", email);
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
                Worker check = getByEmail(item.getEmail());
                
                if(check != null && (item.getId() == null || item.getId() != check.getId()))
                {
                    throw new IllegalArgumentException("Worker already exists with email " + item.getEmail());
                }
                
		LOGGER.warn("Saving worker " + item);
		ofy().save().entity(item).now();
	}

	public Worker getByUser(User user) {
		if (user != null) {
			return getByEmail(user.getEmail());
		}
		return null;
	}
}
