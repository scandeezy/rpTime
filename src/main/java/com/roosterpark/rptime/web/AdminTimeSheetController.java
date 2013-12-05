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
import com.roosterpark.rptime.model.TimeSheet;
import com.roosterpark.rptime.model.TimeSheetView;
import com.roosterpark.rptime.model.Worker;
import com.roosterpark.rptime.service.TimeSheetService;
import com.roosterpark.rptime.service.WorkerService;

/**
 * {@link Controller} responsible for <b>Administrator-only</b> {@link TimeSheet} ({@link TimeSheetView})-related MVC endpoints of the form
 * {@code /admin/timesheet}.
 * <p>
 * Per {@code web.xml}, Administrator-only endpoint {@link RequestMapping RequestMappings} are secured by having {@code /admin/} in their
 * URL path.
 * 
 * @author jjzabkar
 */
@Controller
@RequestMapping(value = "/admin/timesheet")
public class AdminTimeSheetController {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	private static final String WORKER_MODEL_ATTRIBUTE_NAME = "worker";
	private static final String CLIENT_ID_PARAMETER_NAME = "clientId";
	private static final String WORKER_ID_PARAMETER_NAME = "workerId";

	@Inject
	TimeSheetController timeSheetControllerDelegate;
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
	public Worker initWorkerBeforeInvokingHandlerMethod(HttpServletRequest request) {
		return timeSheetControllerDelegate.initWorkerBeforeInvokingHandlerMethod(request);
	}

	@RequestMapping(value = "{id}", method = DELETE)
	@ResponseStatus(NO_CONTENT)
	public void deleteId(@PathVariable("id") Long id) {
		service.delete(id);
	}

	@RequestMapping(method = GET)
	@ResponseBody
	public List<TimeSheetView> getAll() {
		return service.getAllAdmin();
	}

	@RequestMapping(value = "client/{clientId}", method = GET)
	@ResponseBody
	public List<TimeSheetView> getAllForClientId(@PathVariable(CLIENT_ID_PARAMETER_NAME) final Long clientId) {
		LOGGER.debug("getAll for clientId={}", clientId);
		return service.getAllForClient(clientId);
	}

	@RequestMapping(value = "worker/{workerId}", method = GET)
	@ResponseBody
	public List<TimeSheetView> getAllForWorkerId(@PathVariable(WORKER_ID_PARAMETER_NAME) final Long workerId) {
		LOGGER.debug("getAll for workerId={}", workerId);
		return service.getAllForWorker(workerId);
	}

	@RequestMapping(value = "{id}", method = GET)
	@ResponseBody
	public TimeSheetView getId(@PathVariable Long id) {
		return service.getById(id);
	}

	@RequestMapping(value = "new", method = GET)
	@ResponseBody
	public TimeSheetView getNew(@ModelAttribute(WORKER_MODEL_ATTRIBUTE_NAME) Worker worker) {
		timeSheetControllerDelegate.validateWorkerOrThrowWorkerNotFoundException(worker);
		return service.getForWorkerDate(worker.getId(), new LocalDate());
	}

	@RequestMapping(value = "status/worker/{workerId}", method = GET)
	@ResponseBody
	public List<TimeSheet> getStatusPerWorkerDate(@PathVariable("workerId") Long workerId) {
		return service.getStatusPerWorker(workerId);
	}

	@RequestMapping(value = "worker/{workerId}/date/{date}", method = GET)
	@ResponseBody
	public TimeSheetView getWorkerIdDate(@PathVariable("workerId") Long workerId, @PathVariable("date") String dateString) {
		final LocalDate date = new LocalDate(dateString);
		LOGGER.debug("workerId={}, date={}", workerId, date);
		return service.getForWorkerDate(workerId, date);
	}

	@RequestMapping(method = POST)
	@ResponseBody
	public TimeSheetView post(@RequestBody TimeSheetView item) {
		LOGGER.debug("saving TimeSheetView {}", item);
		TimeSheetView view = service.set(item);
		return view;
	}

	/**
	 * Set the {@code flagged} attribute for a {@link Worker} with id={@code id}.
	 */
	@RequestMapping(value = "flag/{id}/{flagged}", method = POST)
	@ResponseBody
	public void postIdFlagged(@PathVariable("id") Long id, @PathVariable("flagged") String flagged) {
		LOGGER.debug("flag to {} timesheet {}", flagged, id);
		Boolean b = Boolean.valueOf(flagged);
		service.flag(id, b);
	}

	@RequestMapping(value = "submit/{id}", method = POST)
	@ResponseBody
	public void postSubmitId(@PathVariable("id") Long id) {
		LOGGER.debug("submit timesheet {}", id);
		service.submit(id);
	}

	@RequestMapping(value = "{id}", method = PUT)
	@ResponseStatus(ACCEPTED)
	@ResponseBody
	public TimeSheetView putId(@PathVariable("id") Long id, @RequestBody TimeSheetView item) {
		Validate.isTrue(item.getId().equals(id));
		service.set(item);
		return item;
	}

}
