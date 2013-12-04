package com.roosterpark.rptime.web;

import static com.roosterpark.rptime.web.ErrorController.ERROR_VIEW_NAME;
import static com.roosterpark.rptime.web.ErrorController.STATUS_CODE_ATTRIBUTE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.roosterpark.rptime.exceptions.WorkerNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	public static final String EXCEPTION_ATTRIBUTE = "exception";

	public GlobalExceptionHandler() {
		LOGGER.info("init");
	}

	/**
	 * Responses will be redirected to {@code ErrorController}'s request mappings to add the {@code exception} and other fields in a
	 * standardized manner.
	 */
	@ExceptionHandler(WorkerNotFoundException.class)
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	@ResponseBody
	public ModelAndView handleWorkerNotFoundException(WorkerNotFoundException ex) {
		LOGGER.debug("forbidden workerNotFoundException={}", ex);
		ModelAndView result = new ModelAndView(ERROR_VIEW_NAME);
		result.addObject(EXCEPTION_ATTRIBUTE, ex);
		result.addObject(STATUS_CODE_ATTRIBUTE, HttpStatus.FORBIDDEN);
		return result;
	}

	/**
	 * Responses will be redirected to {@code ErrorController}'s request mappings to add the {@code exception} and other fields in a
	 * standardized manner.
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ModelAndView handleException(Exception ex) {
		LOGGER.debug("bad request exception={}", ex);
		ModelAndView result = new ModelAndView(ERROR_VIEW_NAME);
		result.addObject(EXCEPTION_ATTRIBUTE, ex);
		result.addObject(STATUS_CODE_ATTRIBUTE, HttpStatus.BAD_REQUEST);
		return result;
	}

}
