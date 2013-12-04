package com.roosterpark.rptime.web;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.Validate;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.appengine.api.users.UserService;
import com.roosterpark.rptime.exceptions.WorkerNotFoundException;
import com.roosterpark.rptime.model.TimeSheet;
import com.roosterpark.rptime.model.TimeSheetView;
import com.roosterpark.rptime.model.Worker;
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
public class TimeSheetController {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	private static final String WORKER_MODEL_ATTRIBUTE_NAME = "worker";

	@Inject
	TimeSheetService service;
	@Inject
	UserService userService;
	@Inject
	WorkerService workerService;

	/**
	 * Method interceptor that sets the {@code worker} {@link ModelAttribute} <i>prior</i> to invoking the {@link RequestMapping} handler
	 * methods.
	 * 
	 * @return the {@link Worker} attribute attached to the {@code HttpServletRequest request} by {@code WorkerFilter}.
	 */
	@ModelAttribute(WORKER_MODEL_ATTRIBUTE_NAME)
	public Worker getWorkerBeforeInvokingHandlerMethod(HttpServletRequest request) {
		return (Worker) request.getAttribute(WORKER_MODEL_ATTRIBUTE_NAME);
	}

	@RequestMapping(value = "/admin/timesheet/new", method = { POST, GET })
	@ResponseBody
	public TimeSheetView getNew(@ModelAttribute(WORKER_MODEL_ATTRIBUTE_NAME) Worker worker) {
		validateWorkerOrThrowWorkerNotFoundException(worker);
		return getTimeSheetForDate(worker, new LocalDate());
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

	@RequestMapping(value = "/admin/timesheet/{id}", method = GET)
	@ResponseBody
	public TimeSheetView getByAdminId(@PathVariable Long id) {
		return service.getById(id);
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
	public List<TimeSheetView> getAll(@ModelAttribute(WORKER_MODEL_ATTRIBUTE_NAME) Worker worker) throws WorkerNotFoundException {
		LOGGER.debug("ModelAttribute worker={}", worker);
		final Long id = workerService.getValidatedWorker().getId();
		return service.getAllForWorker(id);
	}

	@RequestMapping(value = "/timesheet/current", method = { GET })
	@ResponseBody
	public TimeSheetView getCurrent(@ModelAttribute(WORKER_MODEL_ATTRIBUTE_NAME) Worker worker) throws WorkerNotFoundException {
		validateWorkerOrThrowWorkerNotFoundException(worker);
		return getTimeSheetForDate(worker, new LocalDate());
	}

	@RequestMapping(value = "/timesheet/last", method = { GET })
	@ResponseBody
	public TimeSheetView getLast(@ModelAttribute(WORKER_MODEL_ATTRIBUTE_NAME) Worker worker) {
		validateWorkerOrThrowWorkerNotFoundException(worker);
		final LocalDate d = (new LocalDate()).minusWeeks(1);
		return getTimeSheetForDate(worker, d);
	}

	@RequestMapping(value = "/timesheet/next", method = { GET })
	@ResponseBody
	public TimeSheetView getNext(@ModelAttribute(WORKER_MODEL_ATTRIBUTE_NAME) Worker worker) {
		validateWorkerOrThrowWorkerNotFoundException(worker);
		final LocalDate d = (new LocalDate()).plusWeeks(1);
		return getTimeSheetForDate(worker, d);
	}

	@RequestMapping(value = "/timesheet/new/{date}", method = { POST, GET })
	@ResponseBody
	public TimeSheetView getDate(@PathVariable("date") String dateString, @ModelAttribute(WORKER_MODEL_ATTRIBUTE_NAME) Worker worker) {
		validateWorkerOrThrowWorkerNotFoundException(worker);
		LocalDate date = new LocalDate(dateString);
		return getTimeSheetForDate(worker, date);
	}

	@RequestMapping(value = "/timesheet/{id}", method = GET)
	@ResponseBody
	public TimeSheetView getById(@PathVariable Long id, @ModelAttribute(WORKER_MODEL_ATTRIBUTE_NAME) Worker worker) {
		validateWorkerOrThrowWorkerNotFoundException(worker);
		final Long workerId = worker.getId();
		TimeSheetView result = service.getById(workerId);
		LOGGER.debug("model workerId={}, timeSheet.workerId={}", workerId, result.getWorkerId());
		// Validate.isTrue(workerId.equals(result.getWorkerId()),"Worker is only permitted to get ");
		return result;
	}

	@RequestMapping(value = "/timesheet", method = POST)
	@ResponseBody
	public TimeSheetView post(@RequestBody TimeSheetView item, @ModelAttribute(WORKER_MODEL_ATTRIBUTE_NAME) Worker worker) {
		validateWorkerOrThrowWorkerNotFoundException(worker);
		final Long workerId = worker.getId();
		LOGGER.debug("saving TimeSheetView {}", item);
		Validate.isTrue(workerId.equals(item.getWorkerId()));
		TimeSheetView view = service.set(item);
		return view;
	}

	@RequestMapping(value = "/admin/timesheet", method = POST)
	@ResponseBody
	public TimeSheetView postAdmin(@RequestBody TimeSheetView item) {
		LOGGER.debug("saving TimeSheetView {}", item);
		TimeSheetView view = service.set(item);
		return view;
	}

	// private helper methods

	// TODO Jackson doesn't like using the Joda Constructor.
	private TimeSheetView getTimeSheetForDate(final Worker worker, final LocalDate date) {
		return service.getForWorkerDate(worker.getId(), date);
	}

	/**
	 * Validate a {@link Worker} is present.
	 * 
	 * @throws WorkerNotFoundException
	 *             if {@code worker} is null
	 */
	private void validateWorkerOrThrowWorkerNotFoundException(final Worker worker) throws WorkerNotFoundException {
		if (worker == null) {
			throw new WorkerNotFoundException("Worker required for this operation", userService.getCurrentUser());
		}
	}

}
