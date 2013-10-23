package com.roosterpark.rptime.service;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.googlecode.objectify.ObjectifyService;
import com.roosterpark.rptime.model.Contract;
import com.roosterpark.rptime.model.TimeSheet;
import com.roosterpark.rptime.model.Worker;

@Named
public class TimeSheetService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Inject
	UserService userService;

	@Inject
	WorkerService workerService;

	@Inject
	ContractService contractService;

	public TimeSheetService() {
		LOGGER.trace("registering TimeSheet class with ObjectifyService");
		ObjectifyService.register(TimeSheet.class);
		LOGGER.trace("registered TimeSheet");
	}

	public TimeSheet create() {
		Worker w = workerService.getByUser(userService.getCurrentUser());
		if (w != null) {
			TimeSheet result = new TimeSheet(w.getId());
			result.setStartDate(new LocalDate());
			LOGGER.debug("created new TimeSheet for worker={},timesheet={}", w, result);
			return result;
		}
		throw new IllegalArgumentException("Worker required for '" + userService.getCurrentUser() + "'");
	}

	public List<TimeSheet> getAll() {
		final User currentUser = userService.getCurrentUser();
		final boolean isAdmin = userService.isUserAdmin();
		if (currentUser != null) {
			final String workerId = currentUser.getEmail();
			if (isAdmin) {
				LOGGER.warn("Getting TimeSheets for admin");
				return ofy().load().type(TimeSheet.class).list();
			} else {
				LOGGER.warn("Getting TimeSheets for workerId={}", workerId);
				return ofy().load().type(TimeSheet.class).filter("workerId", workerId).list();
			}

		}
		return null;
	}

	public TimeSheet getById(Long id) {
		return ofy().load().type(TimeSheet.class).id(id).now();
	}

	public List<TimeSheet> getClientWeekPage(String client, Integer week, int count, int offset) {
		return ofy().load().type(TimeSheet.class).filter(TimeSheet.CLIENT_KEY, client).filter(TimeSheet.WEEK_KEY, week).limit(count)
				.offset(offset).list();
	}

	public List<TimeSheet> getWorkerWeekPage(String worker, Integer week, int count, int offset) {
		return ofy().load().type(TimeSheet.class).filter(TimeSheet.WORKER_KEY, worker).filter(TimeSheet.WEEK_KEY, week).limit(count)
				.offset(offset).list();
	}

	public List<TimeSheet> getPage(int count, int offset) {
		return ofy().load().type(TimeSheet.class).limit(count).offset(offset).list();
	}

	public void set(TimeSheet item) {
		ofy().save().entity(item).now();
	}

	public List<TimeSheet> getRecentForWorker(Long workerId, Date date, int limit) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		Integer week = cal.get(Calendar.WEEK_OF_YEAR);
		LOGGER.debug("querying Sheets for worker={},date={},week={}", workerId, date, week);
		List<TimeSheet> result = ofy()//
				.load()//
				.type(TimeSheet.class)//
				.filter(TimeSheet.WORKER_KEY, workerId)//
				.filter(TimeSheet.WEEK_KEY + " <=", week)//
				.limit(limit)//
				// .order("-date")//Descending date sort
				.list();
		if (CollectionUtils.isEmpty(result)) {
			LOGGER.debug("creating new Sheet for worker={},date={},week={}", workerId, date, week);
			List<Contract> contracts = contractService.getContractsForWorker(workerId);
			result = new LinkedList<TimeSheet>();
			// for (Contract contract : contracts) {
			// TimeSheet s = new TimeSheet(workerId, contract., week);
			// set(s); // persist;
			// LOGGER.debug("adding sheet to list: sheet={}", s);
			// result.add(s);
			// }
		}
		LOGGER.debug("returning List<Sheet>={}", result);
		return result;
	}

}
