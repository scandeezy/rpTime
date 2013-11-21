package com.roosterpark.rptime.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.users.User;
import com.roosterpark.rptime.model.Worker;

@Named
public class WorkerService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	public Worker getById(Long id) {
		LOGGER.warn("Getting worker with id={}", id);
		final Worker result = ofy().load().type(Worker.class).id(id).now();
		if (result == null) {
			LOGGER.warn("Warning: no Worker found for id='{}'", id);
		}
		return result;
	}

	public Worker getByEmail(final String email) {
		final String emailLower = StringUtils.lowerCase(email);
		LOGGER.warn("Getting worker with email={} ", emailLower);
		final Worker result = ofy().load().type(Worker.class)//
				.filter(Worker.EMAIL_KEY, emailLower)//
				.first()//
				.now();
		if (result == null) {
			LOGGER.warn("Warning: no Worker found for email='{}'", emailLower);
		}
		return result;
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
		if (StringUtils.isEmpty(item.getEmail()))
			throw new IllegalArgumentException("Email required.");
		if (StringUtils.isEmpty(item.getFirstName()))
			throw new IllegalArgumentException("First name required.");
		if (StringUtils.isEmpty(item.getLastName()))
			throw new IllegalArgumentException("Last name required.");

		Worker check = getByEmail(item.getEmail());

		if (check != null && (item.getId() == null || !item.getId().equals(check.getId()))) {
			throw new IllegalArgumentException("Worker already exists with email " + item.getEmail() + " and id " + check.getId()
					+ ", provided id " + item.getId());
		}

		// Default to true
		if (item.getActive() == null)
			item.setActive(true);
		if (item.getHourly() == null)
			item.setHourly(false);

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
