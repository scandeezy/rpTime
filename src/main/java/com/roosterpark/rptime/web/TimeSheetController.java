package com.roosterpark.rptime.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

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

import com.google.appengine.api.users.UserService;
import com.roosterpark.rptime.exceptions.WorkerNotFoundException;
import com.roosterpark.rptime.model.TimeSheet;
import com.roosterpark.rptime.model.TimeSheetView;
import com.roosterpark.rptime.model.Worker;
import com.roosterpark.rptime.service.TimeSheetService;
import com.roosterpark.rptime.service.WorkerService;

/**
 * {@link Controller} responsible for {@link TimeSheet} ({@link TimeSheetView})-related MVC endpoints.
 * 
 * @author jjzabkar
 */
@Controller
@RequestMapping(value = "/timesheet")
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
	public Worker initWorkerBeforeInvokingHandlerMethod(HttpServletRequest request) {
		return (Worker) request.getAttribute(WORKER_MODEL_ATTRIBUTE_NAME);
	}

	@RequestMapping(method = GET)
	@ResponseBody
	public List<TimeSheetView> getAll(@ModelAttribute(WORKER_MODEL_ATTRIBUTE_NAME) Worker worker) throws WorkerNotFoundException {
		LOGGER.debug("ModelAttribute worker={}", worker);
		final Long id = workerService.getValidatedWorker().getId();
		return service.getAllForWorker(id);
	}

	@RequestMapping(value = "current", method = GET)
	@ResponseBody
	public TimeSheetView getCurrent(@ModelAttribute(WORKER_MODEL_ATTRIBUTE_NAME) Worker worker) throws WorkerNotFoundException {
		validateWorkerOrThrowWorkerNotFoundException(worker);
		return getTimeSheetForDate(worker, new LocalDate());
	}

	@RequestMapping(value = "{id}", method = GET)
	@ResponseBody
	public TimeSheetView getId(@PathVariable Long id, @ModelAttribute(WORKER_MODEL_ATTRIBUTE_NAME) Worker worker) {
		validateWorkerOrThrowWorkerNotFoundException(worker);
		final Long workerId = worker.getId();
		TimeSheetView result = service.getById(workerId);
		LOGGER.debug("model workerId={}, timeSheet.workerId={}", workerId, result.getWorkerId());
		// TODO: Validate.isTrue(workerId.equals(result.getWorkerId()),"Worker is only permitted to get ");
		return result;
	}

	@RequestMapping(value = "last", method = GET)
	@ResponseBody
	public TimeSheetView getLast(@ModelAttribute(WORKER_MODEL_ATTRIBUTE_NAME) Worker worker) {
		validateWorkerOrThrowWorkerNotFoundException(worker);
		final LocalDate d = (new LocalDate()).minusWeeks(1);
		return getTimeSheetForDate(worker, d);
	}

	@RequestMapping(value = "new/{date}", method = GET)
	@ResponseBody
	public TimeSheetView getNewDate(@PathVariable("date") String dateString, @ModelAttribute(WORKER_MODEL_ATTRIBUTE_NAME) Worker worker) {
		validateWorkerOrThrowWorkerNotFoundException(worker);
		LocalDate date = new LocalDate(dateString);
		return getTimeSheetForDate(worker, date);
	}

	@RequestMapping(value = "next", method = GET)
	@ResponseBody
	public TimeSheetView getNext(@ModelAttribute(WORKER_MODEL_ATTRIBUTE_NAME) Worker worker) {
		validateWorkerOrThrowWorkerNotFoundException(worker);
		final LocalDate d = (new LocalDate()).plusWeeks(1);
		return getTimeSheetForDate(worker, d);
	}

	@RequestMapping(method = POST)
	@ResponseBody
	public TimeSheetView post(@RequestBody TimeSheetView item, @ModelAttribute(WORKER_MODEL_ATTRIBUTE_NAME) Worker worker) {
		validateWorkerOrThrowWorkerNotFoundException(worker);
		final Long workerId = worker.getId();
		LOGGER.debug("saving TimeSheetView {}", item);
		Validate.isTrue(workerId.equals(item.getWorkerId()));
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
			throw new WorkerNotFoundException("Worker required for this operation.", workerService);
		}
	}

}
