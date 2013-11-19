package com.roosterpark.rptime.web;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.roosterpark.rptime.model.TimeSheetView;
import com.roosterpark.rptime.model.Worker;
import com.roosterpark.rptime.service.TimeSheetService;
import com.roosterpark.rptime.service.WorkerService;

@Controller
@RequestMapping(value = "/timesheet")
public class TimeSheetController {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Inject
	TimeSheetService service;
	@Inject
	UserService userService;
	@Inject
	WorkerService workerService;

	/**
	 * Validate the session's {@link User} (via {@link UserService}).
	 * 
	 * @return a {@link Worker} keyed on the session {@link User User's} {@code email}.
	 * @throws IllegalArgumentException
	 *             if the {@code User} is any of the following:
	 *             <ol>
	 *             <li>Not logged in
	 *             <li>Logged in, but not associated with a {@code User}
	 *             <li>Missing an {@code email}
	 *             <li>Not associated with a {@code Worker} (via the {@code email} field)
	 */
	private Worker getValidatedWorker() throws IllegalArgumentException {
		if (userService.isUserLoggedIn()) {
			User user = userService.getCurrentUser();
			if (user != null) {
				final String email = user.getEmail();
				if (StringUtils.isNotEmpty(email)) {
					Worker worker = workerService.getByEmail(email);
					if (worker != null) {
						return worker;
					}
					throw new IllegalArgumentException("No Worker found for email '" + email
							+ "'.  To resolve, create a Worker with this email to link it to a user.");
				}
				throw new IllegalArgumentException("The required email for user '" + user.toString() + "' [hash=" + user.hashCode()
						+ "] is empty.");
			}
			throw new IllegalArgumentException("Someone is logged in, but it's not a user.");
		}
		throw new IllegalArgumentException("User not logged in.");
	};

	@RequestMapping(value = "/new", method = { POST, GET })
	@ResponseBody
	public TimeSheetView create() {
		// TODO: lock down further by isUserAdmin
		return getTimeSheetForDate(new LocalDate());
	}

	@RequestMapping(value = "/flag/{id}/{flagged}", method = POST)
	@ResponseBody
	public void flag(@PathVariable("id") Long id, @PathVariable("flagged") String flagged) {
		LOGGER.debug("flag to {} timesheet {}", flagged, id);
		Boolean b = Boolean.valueOf(flagged);
		service.flag(id, b);
	}

	@RequestMapping(value = "/current", method = { GET })
	@ResponseBody
	public TimeSheetView getCurrent(@RequestParam(value = "workerId", required = false) String workerId) {
		Long id;
		if (workerId != null) {
			id = Long.parseLong(workerId);
		} else {
			id = getValidatedWorker().getId();
		}
		return service.getCurrentForWorker(id);
	}

	@RequestMapping(value = "/last", method = { GET })
	@ResponseBody
	public TimeSheetView getLast() {
		final LocalDate d = (new LocalDate()).minusWeeks(1);
		// TODO: lock down further by isUserAdmin
		return getTimeSheetForDate(d);
	}

	@RequestMapping(value = "/next", method = { GET })
	@ResponseBody
	public TimeSheetView getNext() {
		final LocalDate d = (new LocalDate()).plusWeeks(1);
		// TODO: lock down further by isUserAdmin
		return getTimeSheetForDate(d);
	}

	@RequestMapping(value = "/new/{date}", method = { POST, GET })
	@ResponseBody
	public TimeSheetView getDate(@PathVariable("date") String dateString) {
		LocalDate date = new LocalDate(dateString);
		return getTimeSheetForDate(date);
	}

	// TODO Jackson doesn't like using the Joda Constructor.
	private TimeSheetView getTimeSheetForDate(LocalDate date) {
		final Worker worker = getValidatedWorker();
		// TODO: lock down further by isUserAdmin
		return service.getForWorkerDate(worker.getId(), date);
	}

	@RequestMapping(method = GET)
	@ResponseBody
	public List<TimeSheetView> getAll() {
		final Worker worker = getValidatedWorker();
		return service.getAll(worker.getId(), userService.isUserAdmin());
	}

	@RequestMapping(value = "/{id}", method = GET)
	@ResponseBody
	public TimeSheetView getById(@PathVariable Long id) {
		// TODO: lock down further by isUserAdmin
		return service.getById(id);
	}

	@RequestMapping(method = POST)
	@ResponseBody
	public TimeSheetView post(@RequestBody TimeSheetView item) {
		// TODO: lock down further by isUserAdmin
		LOGGER.debug("saving TimeSheetView {}", item);
		TimeSheetView view = service.set(item);
		return view;
	}

	@RequestMapping(value = "/submit/{id}", method = POST)
	@ResponseBody
	public void submit(@PathVariable("id") Long id) {
		LOGGER.debug("submit timesheet {}", id);
		service.submit(id);
	}

	@RequestMapping(value = "/{id}", method = PUT)
	@ResponseStatus(ACCEPTED)
	@ResponseBody
	public TimeSheetView put(@PathVariable("id") Long id, @RequestBody TimeSheetView item) {
		Validate.isTrue(item.getId().equals(id));
		service.set(item);
		return item;
	}

	@RequestMapping(value = "/{id}", method = DELETE)
	@ResponseStatus(NO_CONTENT)
	public void delete(@PathVariable("id") Long id) {
		service.delete(id);
	}

}
