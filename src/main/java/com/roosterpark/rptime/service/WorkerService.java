package com.roosterpark.rptime.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.users.User;
import com.roosterpark.rptime.model.Worker;
import org.datanucleus.util.StringUtils;

@Named
public class WorkerService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

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
                // TODO: separate the business logic of the service from the DAO
		// TODO: Cache better so we don't have to do this. Costly!
		ofy().clear();
		// ^ Argh

		return ofy().load().type(Worker.class).list();
	}

	public void set(Worker item) {
                if(StringUtils.isEmpty(item.getEmail()))
                    throw new IllegalArgumentException("Email required.");
                if(StringUtils.isEmpty(item.getFirstName()))
                    throw new IllegalArgumentException("First name required.");
                if(StringUtils.isEmpty(item.getLastName()))
                    throw new IllegalArgumentException("Last name required.");
                
		Worker check = getByEmail(item.getEmail());

		if (check != null && (item.getId() == null || item.getId() != check.getId())) {
			throw new IllegalArgumentException("Worker already exists with email " + item.getEmail());
		}

		// Default to true
		if (item.getActive() == null)
			item.setActive(Boolean.TRUE);

		LOGGER.warn("Saving worker " + item);
		ofy().save().entity(item).now();
	}

	public Worker getByUser(User user) {
		if (user != null) {
			return getByEmail(user.getEmail());
		}
		return null;
	}

	public void delete(Long id) {
		Worker w = getById(id);
		LOGGER.debug("Delete Worker {}", id);
		ofy().delete().entity(w).now();
	}
}
