package com.roosterpark.rptime.web;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.Validate;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.appengine.api.users.UserService;
import com.roosterpark.rptime.model.TimeSheet;
import com.roosterpark.rptime.model.TimeSheetView;
import com.roosterpark.rptime.service.TimeSheetService;
import com.roosterpark.rptime.service.WorkerService;

/**
 * {@link Controller} responsible for {@link TimeSheet} ({@link TimeSheetView})-related MVC endpoints.
 * <p>
 * Per {@code web.xml} Administrator-only endpoint {@link RequestMapping RequestMappings} are secured by having {@code /admin/} in their URL
 * path.
 * 
 * @author jjzabkar
 */
@Controller
// @RequestMapping(value = "/admin/timesheet/timesheet")
public class TimeSheetController {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Inject
	TimeSheetService service;
	@Inject
	UserService userService;
	@Inject
	WorkerService workerService;

	@RequestMapping(value = "/admin/timesheet/new", method = { POST, GET })
	@ResponseBody
	public TimeSheetView getNew() {
		return getTimeSheetForDate(new LocalDate());
	}

	@RequestMapping(value = "/admin/timesheet/flag/{id}/{flagged}", method = POST)
	@ResponseBody
	public void flag(@PathVariable("id") Long id, @PathVariable("flagged") String flagged) {
		LOGGER.debug("flag to {} timesheet {}", flagged, id);
		Boolean b = Boolean.valueOf(flagged);
		service.flag(id, b);
	}

	@RequestMapping(value = "/admin/timesheet", method = GET)
	@ResponseBody
	public List<TimeSheetView> getAllAdmin() {
		return service.getAllAdmin();
	}

	@RequestMapping(value = "/admin/timesheet/submit/{id}", method = POST)
	@ResponseBody
	public void submit(@PathVariable("id") Long id) {
		LOGGER.debug("submit timesheet {}", id);
		service.submit(id);
	}

	@RequestMapping(value = "/admin/timesheet/{id}", method = PUT)
	@ResponseStatus(ACCEPTED)
	@ResponseBody
	public TimeSheetView put(@PathVariable("id") Long id, @RequestBody TimeSheetView item) {
		Validate.isTrue(item.getId().equals(id));
		service.set(item);
		return item;
	}

	@RequestMapping(value = "/admin/timesheet/{id}", method = DELETE)
	@ResponseStatus(NO_CONTENT)
	public void delete(@PathVariable("id") Long id) {
		service.delete(id);
	}

	@RequestMapping(value = "/admin/timesheet/client/{clientId}", method = GET)
	@ResponseBody
	public List<TimeSheetView> getAllForClientId(@PathVariable("clientId") final Long clientId) {
		LOGGER.debug("getAll for clientId={}", clientId);
		return service.getAllForClient(clientId);
	}

	@RequestMapping(value = "/admin/timesheet/worker/{workerId}", method = GET)
	@ResponseBody
	public List<TimeSheetView> getAllForWorkerId(@PathVariable("workerId") final Long workerId) {
		LOGGER.debug("getAll for workerId={}", workerId);
		return service.getAllForWorker(workerId);
	}

	// non-admin functions

	@RequestMapping(value = "/timesheet", method = GET)
	@ResponseBody
	public List<TimeSheetView> getAll() {
		final Long id = workerService.getValidatedWorker().getId();
		return service.getAllForWorker(id);
	}

	@RequestMapping(value = "/timesheet/current", method = GET)
	@ResponseBody
	public TimeSheetView getCurrent() {
		final Long id = workerService.getValidatedWorkerId();
		return service.getCurrentForWorker(id);
	}

	@RequestMapping(value = "/timesheet/current/worker/{workerId}", method = GET)
	@ResponseBody
	public TimeSheetView getCurrentForWorker(final Long workerId) {
		return service.getCurrentForWorker(workerId);
	}

	@RequestMapping(value = "/timesheet/last", method = { GET })
	@ResponseBody
	public TimeSheetView getLast() {
		final LocalDate d = (new LocalDate()).minusWeeks(1);
		return getTimeSheetForDate(d);
	}

	@RequestMapping(value = "/timesheet/next", method = { GET })
	@ResponseBody
	public TimeSheetView getNext() {
		final LocalDate d = (new LocalDate()).plusWeeks(1);
		return getTimeSheetForDate(d);
	}

	@RequestMapping(value = "/timesheet/new/{date}", method = { POST, GET })
	@ResponseBody
	public TimeSheetView getDate(@PathVariable("date") String dateString) {
		LocalDate date = new LocalDate(dateString);
		return getTimeSheetForDate(date);
	}

	@RequestMapping(value = "/timesheet/{id}", method = GET)
	@ResponseBody
	public TimeSheetView getById(@PathVariable Long id) {
		final Long workerId = workerService.getValidatedWorkerId();
		final TimeSheetView result = service.getById(id);
		Validate.isTrue(workerId.equals(result.getWorkerId()));
		return result;
	}

	@RequestMapping(value = "/timesheet", method = POST)
	@ResponseBody
	public TimeSheetView post(@RequestBody TimeSheetView item) {
		LOGGER.debug("saving TimeSheetView {}", item);
		TimeSheetView view = service.set(item);
		return view;
	}

	// private helper methods

	// TODO Jackson doesn't like using the Joda Constructor.
	private TimeSheetView getTimeSheetForDate(LocalDate date) {
		final Long id = workerService.getValidatedWorkerId();
		return service.getForWorkerDate(id, date);
	}

}
