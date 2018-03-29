package com.roosterpark.rptime.web;

import static com.roosterpark.rptime.web.GlobalExceptionHandler.EXCEPTION_ATTRIBUTE;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.roosterpark.rptime.exceptions.WorkerNotFoundException;

/**
 * {@link Controller} that exposes common endpoint {@link RequestMapping RequestMappings} to add error fields in a standardized manner.
 * 
 * @author jjzabkar
 * @see {@link GlobalExceptionHandler}
 */
@Controller
public class ErrorController {
	Logger LOGGER = LoggerFactory.getLogger(getClass());

	public static final String ERROR_VIEW_NAME = "error";
	public static final String STATUS_CODE_ATTRIBUTE = "javax.servlet.error.status_code";

	@RequestMapping(value = ERROR_VIEW_NAME)
	@ResponseBody
	public Map<String, Object> handle(HttpServletRequest request) {
		final Map<String, Object> map = new HashMap<String, Object>(4);
		map.put(EXCEPTION_ATTRIBUTE, request.getAttribute(EXCEPTION_ATTRIBUTE));
		Object reason = request.getAttribute("javax.servlet.error.message");
		Object status = request.getAttribute(STATUS_CODE_ATTRIBUTE);
		map.put("reason", reason);
		map.put("status", status);
		map.put("timestamp", new LocalDateTime());
		return map;
	}

}