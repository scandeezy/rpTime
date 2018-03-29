package com.roosterpark.rptime.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.users.User;
import com.roosterpark.rptime.model.UnlinkedUser;
import com.roosterpark.rptime.model.Worker;

@Named
public class UnlinkedUserService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Inject
	WorkerService workerService;

	/**
	 * Increments the {@link User currentUser's} {@code workerNotFoundExceptionCount} for the persisted {@link UnlinkedUser}.
	 * 
	 * @param currentUser
	 */
	public void logWorkerNotFoundException(final User currentUser) {
		LOGGER.debug("logging UnlinkedUser {}", currentUser);
		if (currentUser != null) {
			final String userId = currentUser.getUserId();
			UnlinkedUser result = ofy().load().type(UnlinkedUser.class).id(userId).now();
			if (result == null) {
				result = new UnlinkedUser(currentUser);
			}
			result.incrementWorkerNotFoundExceptionCount();
			LOGGER.debug("Saving UnlinkedUser {}", result);
			ofy().save().entity(result).now();
		}
	}

	@SuppressWarnings("unchecked")
	public Collection<UnlinkedUser> getAll() {
		List<UnlinkedUser> users = ofy().load().type(UnlinkedUser.class).list();
		List<Worker> workers = workerService.getAll();
		List<UnlinkedUser> omit = new ArrayList<UnlinkedUser>();
		for (UnlinkedUser u : users) {
			String userEmail = u.getUser().getEmail();
			for (Worker w : workers) {
				String workerEmail = w.getEmail();
				if (StringUtils.equalsIgnoreCase(userEmail, workerEmail)) {
					omit.add(u);
					break;
				}
			}
		}
		return CollectionUtils.subtract(users, omit);
	}

}
