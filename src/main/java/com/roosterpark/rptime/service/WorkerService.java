package com.roosterpark.rptime.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.roosterpark.rptime.exceptions.WorkerNotFoundException;
import com.roosterpark.rptime.model.Worker;

@Named("workerService")
public class WorkerService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	private static WorkerService inst;

	@Inject
	UserService userService;

	public WorkerService() {
		inst = this;
	}

	public static final WorkerService inst() {
		return inst;
	}

	public Worker getCurrent() {
		User user = userService.getCurrentUser();
		if (user != null) {
			final String email = user.getEmail();
			return getByEmail(email);
		}
		return null;
	}

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

	public boolean isCurrentUserWorkerWithId(final Long workerId) {
		Validate.notNull(workerId, "Required: workerId");
		User user = userService.getCurrentUser();
		if (user != null) {
			final String email = user.getEmail();
			Worker worker = getByEmail(email);
			if (worker != null) {
				return workerId.equals(worker.getId());
			}
		}
		return false;
	}

	/**
	 * Get the current {@link Worker} via the session's {@link User} (via {@link UserService}).
	 * 
	 * @return a {@link Worker} keyed on the session {@link User User's} {@code email}.
	 * @throws WorkerNotFoundException
	 *             if the {@code User} is any of the following:
	 *             <ol>
	 *             <li>Not associated with a {@code Worker} (via the {@code email} field)
	 * @throws IllegalArgumentException
	 *             if the {@code User} is any of the following:
	 *             <ol>
	 *             <li>Not logged in <li>Logged in, but not associated with a {@code User} <li>Missing an {@code email}
	 */
	public Worker getValidatedWorker() throws IllegalArgumentException, WorkerNotFoundException {
		if (userService.isUserLoggedIn()) {
			User user = userService.getCurrentUser();
			if (user != null) {
				final String email = user.getEmail();
				Worker worker = getByEmail(email);
				if (worker != null) {
					return worker;
				} else if (userService.isUserAdmin()) {
					return null;
				}
				throw new WorkerNotFoundException("No Worker found for email '" + email + "' for user '" + user.toString()
						+ "'.  To resolve, create a Worker with this email to link it to a user.", user);
			}
			throw new IllegalArgumentException("Someone is logged in, but it's not a user.");
		}
		throw new IllegalArgumentException("User not logged in.");
	};

	public Long getValidatedWorkerId() throws WorkerNotFoundException {
		final Worker w = getValidatedWorker();
		if (w != null) {
			return w.getId();
		} else {
			throw new WorkerNotFoundException("No Worker found.  Unknown error occurred.", userService.getCurrentUser());
		}
	}

}
